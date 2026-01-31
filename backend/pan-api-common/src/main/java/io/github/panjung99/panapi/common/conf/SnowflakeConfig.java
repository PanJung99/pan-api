package io.github.panjung99.panapi.common.conf;

import cn.hutool.core.lang.Snowflake;
import io.github.panjung99.panapi.common.util.IdUtil;
import io.github.panjung99.panapi.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class SnowflakeConfig {

    private static final int SLOT = 1024;

    private static final String REDIS_PREFIX = "snowflake:slot:";

    private static final int HEARTBEAT_INTERVAL = 20;

    private static final int SLOT_EXPIRE_TIME = 120;

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService snowflakeExecutor() {
        return Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "snowflake-hb");
            t.setDaemon(true);
            return t;
        });
    }

    @Bean
    public Snowflake snowflake(StringRedisTemplate redisTemplate,
                               ScheduledExecutorService snowflakeExecutor) {

        String hostname = System.getenv("HOSTNAME");
        String instanceId = hostname + "-" + UUIDUtil.randomUUID();

        // 随机偏移，防止遍历时一直占不到slot
        int randomOffset = ThreadLocalRandom.current().nextInt(SLOT);
        int targetSlot = -1;

        for (int i = 0; i < SLOT; i++) {
            int slot = (i + randomOffset) % SLOT;
            String key = REDIS_PREFIX + slot;
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, instanceId, Duration.ofSeconds(SLOT_EXPIRE_TIME));
            if (Boolean.TRUE.equals(result)) {
                targetSlot = slot;
                break;
            }
        }
        if (targetSlot == -1) {
            throw new RuntimeException("Failed to assign a slot for snowflake instance.");
        }
        startHeartbeat(redisTemplate, snowflakeExecutor, targetSlot, instanceId);

        // 拆分槽位 ID 为雪花算法所需的两个 5 位 ID
        long dataCenterId = (targetSlot >> 5) & 31;
        long workerId = targetSlot & 31;
        return new Snowflake(null, workerId, dataCenterId, false, 10);
    }


    private void startHeartbeat(StringRedisTemplate redisTemplate, ScheduledExecutorService snowflakeExecutor,
                                int slot, String instanceId) {
        String key = REDIS_PREFIX + slot;

        snowflakeExecutor.scheduleAtFixedRate(() -> {
            try {
                // 续期前检查，确保这个槽位还是属于自己的
                String currentOwner = redisTemplate.opsForValue().get(key);
                if (instanceId.equals(currentOwner)) {
                    redisTemplate.expire(key, Duration.ofSeconds(SLOT_EXPIRE_TIME));
                } else {
                    // 两种情况，过期但无人占据:null 过期且有人占据:OtherInstanceId
                    log.warn("Detected Snowflake slot conflicted(slot:{} currentOwner:{} instanceId:{}), attempting to re-acquire...",
                            slot, currentOwner, instanceId);

                    Boolean reAcquire = redisTemplate.opsForValue().setIfAbsent(key, instanceId, Duration.ofSeconds(SLOT_EXPIRE_TIME));
                    if (Boolean.TRUE.equals(reAcquire)) {
                        log.info("Successfully re-acquired snowflake slot: {}", slot);
                        IdUtil.setIsSlotValid(true);
                    } else {
                        // 已经被占
                        log.error("Re-acquire failed. Slot {} is occupied by: {}", slot, currentOwner);
                        IdUtil.setIsSlotValid(false);
                    }
                }
            } catch (Exception e) {
                log.warn("Snowflake slot heartbeat failed for slot {}", slot, e);
            }
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }


}

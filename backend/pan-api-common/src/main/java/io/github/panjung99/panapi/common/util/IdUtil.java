package io.github.panjung99.panapi.common.util;

import cn.hutool.core.lang.Snowflake;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IdUtil {

    private static volatile boolean isSlotValid = true;

    public static void setIsSlotValid(boolean isSlotValid) {
        IdUtil.isSlotValid = isSlotValid;
    }

    public Snowflake snowflake;

    public IdUtil(Snowflake snowflake) {
        this.snowflake = snowflake;
    }

    public String nextIdStr() {
        if (!isSlotValid) {
            log.error("Distributed ID generation service is unavailable, snowflake slot has been occupied, " +
                    "please check Redis connection and restart the instance.");
            throw new AppException(ErrorEnum.SNOW_FLAKE_SLOT_INVALID);
        }

        return snowflake.nextIdStr();
    }

}

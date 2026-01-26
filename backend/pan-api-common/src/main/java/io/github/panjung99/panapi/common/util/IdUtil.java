package io.github.panjung99.panapi.common.util;

import cn.hutool.core.lang.Snowflake;
import org.springframework.stereotype.Component;

@Component
public class IdUtil {

    public static final Snowflake SNOW_FLAKE;
    static {
        SNOW_FLAKE = cn.hutool.core.util.IdUtil.getSnowflake(0, 0);
    }

    public static String nextIdStr() {
        return SNOW_FLAKE.nextIdStr();
    }

}

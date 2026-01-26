package io.github.panjung99.panapi.common.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class MapUtil {
    public static MultiValueMap<String, String> toMultiValueMap(Map<String, String> map) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        if (map != null) {
            map.forEach(multiValueMap::add);
        }
        return multiValueMap;
    }
}

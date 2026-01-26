package io.github.panjung99.panapi.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP"
    };

    /**
     * 获取真实ip
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        for (String header: IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && !ipList.isEmpty() && !"unknown".equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }
        return request.getRemoteAddr();
    }
}
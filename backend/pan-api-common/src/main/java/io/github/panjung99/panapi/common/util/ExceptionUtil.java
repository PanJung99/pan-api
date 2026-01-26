package io.github.panjung99.panapi.common.util;

public class ExceptionUtil {

    public static boolean isClientAbortError(Throwable ex) {
        if (ex == null) return false;
        String message = ex.getMessage();
        if (message == null) return false;

        message = message.toLowerCase(); // 忽略大小写

        return message.contains("broken pipe")
                || message.contains("connection reset")
                || message.contains("connection reset by peer")
                || message.contains("connection aborted")
                || message.contains("software caused connection abort")
                || message.contains("an established connection was aborted");
    }
}

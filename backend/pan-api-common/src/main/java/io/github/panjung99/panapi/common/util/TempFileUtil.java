package io.github.panjung99.panapi.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TempFileUtil {

    /**
     * 从内容创建临时文件，并返回路径
     */
    public static String createTempFileFromContent(String content, String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit(); // JVM退出时删除

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }

        return tempFile.getAbsolutePath();
    }
}

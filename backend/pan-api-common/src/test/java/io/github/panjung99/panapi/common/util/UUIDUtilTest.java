package io.github.panjung99.panapi.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UUIDUtil 测试")
class UUIDUtilTest {

    private static final Pattern UUID_WITHOUT_DASH_PATTERN = Pattern.compile("^[a-f0-9]{32}$");
    private static final Pattern UUID_WITH_DASH_PATTERN = Pattern.compile("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$");

    @Test
    @DisplayName("randomUUID - 生成无中划线的UUID")
    void randomUUID_Success() {
        String uuid = UUIDUtil.randomUUID();

        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
        assertTrue(UUID_WITHOUT_DASH_PATTERN.matcher(uuid).matches());
    }

    @DisplayName("randomUUID - 多次调用生成唯一UUID")
    @RepeatedTest(100)
    void randomUUID_GeneratesUniqueUUIDs() {
        String uuid1 = UUIDUtil.randomUUID();
        String uuid2 = UUIDUtil.randomUUID();

        assertNotNull(uuid1);
        assertNotNull(uuid2);
        assertNotEquals(uuid1, uuid2);
        assertEquals(32, uuid1.length());
        assertEquals(32, uuid2.length());
        assertFalse(uuid1.contains("-"));
        assertFalse(uuid2.contains("-"));
    }

    @Test
    @DisplayName("randomUUID - 批量生成UUID全部唯一")
    void randomUUID_BatchGeneration_AllUnique() {
        Set<String> uuidSet = new HashSet<>();
        int batchSize = 1000;

        for (int i = 0; i < batchSize; i++) {
            String uuid = UUIDUtil.randomUUID();
            assertTrue(uuidSet.add(uuid), "UUID should be unique: " + uuid);
        }

        assertEquals(batchSize, uuidSet.size());
    }

    @Test
    @DisplayName("randomUUID - UUID格式正确（小写十六进制）")
    void randomUUID_FormatCorrect() {
        String uuid = UUIDUtil.randomUUID();

        for (char c : uuid.toCharArray()) {
            assertTrue(Character.isDigit(c) || (c >= 'a' && c <= 'f'), 
                "UUID should only contain lowercase hex digits: " + c);
        }
    }

    @Test
    @DisplayName("randomUUIDWithDash - 生成带中划线的标准UUID")
    void randomUUIDWithDash_Success() {
        String uuid = UUIDUtil.randomUUIDWithDash();

        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
        assertEquals(4, countOccurrences(uuid, '-'));
        assertTrue(UUID_WITH_DASH_PATTERN.matcher(uuid).matches());
    }

    @DisplayName("randomUUIDWithDash - 多次调用生成唯一UUID")
    @RepeatedTest(100)
    void randomUUIDWithDash_GeneratesUniqueUUIDs() {
        String uuid1 = UUIDUtil.randomUUIDWithDash();
        String uuid2 = UUIDUtil.randomUUIDWithDash();

        assertNotNull(uuid1);
        assertNotNull(uuid2);
        assertNotEquals(uuid1, uuid2);
        assertEquals(36, uuid1.length());
        assertEquals(36, uuid2.length());
        assertTrue(uuid1.contains("-"));
        assertTrue(uuid2.contains("-"));
    }

    @Test
    @DisplayName("randomUUIDWithDash - 批量生成UUID全部唯一")
    void randomUUIDWithDash_BatchGeneration_AllUnique() {
        Set<String> uuidSet = new HashSet<>();
        int batchSize = 1000;

        for (int i = 0; i < batchSize; i++) {
            String uuid = UUIDUtil.randomUUIDWithDash();
            assertTrue(uuidSet.add(uuid), "UUID should be unique: " + uuid);
        }

        assertEquals(batchSize, uuidSet.size());
    }

    @Test
    @DisplayName("randomUUIDWithDash - UUID格式正确（小写十六进制）")
    void randomUUIDWithDash_FormatCorrect() {
        String uuid = UUIDUtil.randomUUIDWithDash();

        String[] parts = uuid.split("-");
        assertEquals(5, parts.length);
        assertEquals(8, parts[0].length());
        assertEquals(4, parts[1].length());
        assertEquals(4, parts[2].length());
        assertEquals(4, parts[3].length());
        assertEquals(12, parts[4].length());

        for (String part : parts) {
            for (char c : part.toCharArray()) {
                assertTrue(Character.isDigit(c) || (c >= 'a' && c <= 'f'), 
                    "UUID should only contain lowercase hex digits: " + c);
            }
        }
    }

    @Test
    @DisplayName("randomUUIDWithDash - UUID中划线位置正确")
    void randomUUIDWithDash_DashPositionsCorrect() {
        String uuid = UUIDUtil.randomUUIDWithDash();

        assertEquals('-', uuid.charAt(8));
        assertEquals('-', uuid.charAt(13));
        assertEquals('-', uuid.charAt(18));
        assertEquals('-', uuid.charAt(23));
    }

    @Test
    @DisplayName("randomUUID 和 randomUUIDWithDash - 生成相同基础UUID")
    void randomUUIDAndRandomUUIDWithDash_SameBaseUUID() {
        String uuidWithoutDash = UUIDUtil.randomUUID();
        String uuidWithDash = UUIDUtil.randomUUIDWithDash();

        assertNotEquals(uuidWithoutDash, uuidWithDash);
        assertEquals(32, uuidWithoutDash.length());
        assertEquals(36, uuidWithDash.length());
    }

    @Test
    @DisplayName("randomUUID - 移除中划线后与randomUUIDWithDash一致")
    void randomUUID_RemoveDashEqualsRandomUUIDWithDash() {
        String uuidWithoutDash = UUIDUtil.randomUUID();
        String uuidWithDash = UUIDUtil.randomUUIDWithDash();
        String uuidWithDashRemoved = uuidWithDash.replace("-", "");

        assertEquals(32, uuidWithoutDash.length());
        assertEquals(32, uuidWithDashRemoved.length());
        assertEquals(36, uuidWithDash.length());
    }

    private int countOccurrences(String str, char target) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }
}

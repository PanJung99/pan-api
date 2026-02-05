package io.github.panjung99.panapi.common.util;

import io.github.panjung99.panapi.common.enums.ModelCategory;
import io.github.panjung99.panapi.common.enums.PlatformTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonUtil 测试")
class JsonUtilTest {

    @Test
    @DisplayName("toJson - 成功序列化简单对象")
    void toJson_Success_SimpleObject() {
        String expectedJson = "{\"name\":\"test\",\"value\":123}";
        
        TestObject testObj = new TestObject();
        testObj.setName("test");
        testObj.setValue(123);

        String result = JsonUtil.toJson(testObj);

        assertEquals(expectedJson, result);
    }

    @Test
    @DisplayName("toJson - 成功序列化枚举对象")
    void toJson_Success_EnumObject() {
        TestEnumObject testObj = new TestEnumObject();
        testObj.setCategory(ModelCategory.chat);
        testObj.setPlatformType(PlatformTypeEnum.OPEN_AI);

        String result = JsonUtil.toJson(testObj);

        assertNotNull(result);
        assertTrue(result.contains("chat"));
        assertTrue(result.contains("OPEN_AI"));
    }

    @Test
    @DisplayName("toJson - 成功序列化包含LocalDateTime的对象")
    void toJson_Success_WithLocalDateTime() {
        TestDateTimeObject testObj = new TestDateTimeObject();
        testObj.setName("test");
        testObj.setCreatedAt(LocalDateTime.of(2026, 2, 5, 12, 30, 45));

        String result = JsonUtil.toJson(testObj);

        assertNotNull(result);
        assertTrue(result.contains("2026-02-05 12:30:45"));
    }

    @Test
    @DisplayName("toJson - 成功序列化BigDecimal对象")
    void toJson_Success_BigDecimal() {
        TestBigDecimalObject testObj = new TestBigDecimalObject();
        testObj.setName("test");
        testObj.setPrice(new BigDecimal("123.45"));

        String result = JsonUtil.toJson(testObj);

        assertNotNull(result);
        assertTrue(result.contains("123.45"));
    }

    @Test
    @DisplayName("toJson - BigDecimal不使用科学计数法")
    void toJson_BigDecimal_NoScientificNotation() {
        TestBigDecimalObject testObj = new TestBigDecimalObject();
        testObj.setName("test");
        testObj.setPrice(new BigDecimal("0.00000000123456789"));

        String result = JsonUtil.toJson(testObj);

        assertNotNull(result);
        assertTrue(result.contains("0.00000000123456789"));
        assertFalse(result.contains("E"));
    }

    @Test
    @DisplayName("toJson - 成功序列化Map对象")
    void toJson_Success_MapObject() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 123);
        map.put("key3", true);

        String result = JsonUtil.toJson(map);

        assertNotNull(result);
        assertTrue(result.contains("key1"));
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("true"));
    }

    @Test
    @DisplayName("toJson - null字段不序列化")
    void toJson_NullFieldsNotIncluded() {
        TestObject testObj = new TestObject();
        testObj.setName("test");
        testObj.setValue(null);

        String result = JsonUtil.toJson(testObj);

        assertNotNull(result);
        assertTrue(result.contains("name"));
        assertFalse(result.contains("value"));
    }

    @Test
    @DisplayName("toJson - 空对象序列化")
    void toJson_EmptyObject() {
        TestObject testObj = new TestObject();

        String result = JsonUtil.toJson(testObj);

        assertNotNull(result);
        assertEquals("{}", result);
    }

    @Test
    @DisplayName("toJson - 字符串对象序列化")
    void toJson_StringObject() {
        String result = JsonUtil.toJson("test string");

        assertNotNull(result);
        assertEquals("\"test string\"", result);
    }

    @Test
    @DisplayName("toJson - 数字对象序列化")
    void toJson_NumberObject() {
        String result = JsonUtil.toJson(123);

        assertNotNull(result);
        assertEquals("123", result);
    }

    @Test
    @DisplayName("toJson - 布尔值对象序列化")
    void toJson_BooleanObject() {
        String result = JsonUtil.toJson(true);

        assertNotNull(result);
        assertEquals("true", result);
    }

    @Test
    @DisplayName("toJson - null对象返回null字符串")
    void toJson_NullObject_ReturnsNullString() {
        String result = JsonUtil.toJson(null);

        assertNotNull(result);
        assertEquals("null", result);
    }

    @Test
    @DisplayName("toJson - 复杂嵌套对象序列化")
    void toJson_Success_NestedObject() {
        TestNestedObject innerObj = new TestNestedObject();
        innerObj.setInnerName("inner");
        innerObj.setInnerValue(100);

        TestOuterObject outerObj = new TestOuterObject();
        outerObj.setOuterName("outer");
        outerObj.setNested(innerObj);

        String result = JsonUtil.toJson(outerObj);

        assertNotNull(result);
        assertTrue(result.contains("outer"));
        assertTrue(result.contains("inner"));
        assertTrue(result.contains("100"));
    }

    static class TestObject {
        private String name;
        private Integer value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

    static class TestEnumObject {
        private ModelCategory category;
        private PlatformTypeEnum platformType;

        public ModelCategory getCategory() {
            return category;
        }

        public void setCategory(ModelCategory category) {
            this.category = category;
        }

        public PlatformTypeEnum getPlatformType() {
            return platformType;
        }

        public void setPlatformType(PlatformTypeEnum platformType) {
            this.platformType = platformType;
        }
    }

    static class TestDateTimeObject {
        private String name;
        private LocalDateTime createdAt;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }

    static class TestBigDecimalObject {
        private String name;
        private BigDecimal price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }

    static class TestNestedObject {
        private String innerName;
        private Integer innerValue;

        public String getInnerName() {
            return innerName;
        }

        public void setInnerName(String innerName) {
            this.innerName = innerName;
        }

        public Integer getInnerValue() {
            return innerValue;
        }

        public void setInnerValue(Integer innerValue) {
            this.innerValue = innerValue;
        }
    }

    static class TestOuterObject {
        private String outerName;
        private TestNestedObject nested;

        public String getOuterName() {
            return outerName;
        }

        public void setOuterName(String outerName) {
            this.outerName = outerName;
        }

        public TestNestedObject getNested() {
            return nested;
        }

        public void setNested(TestNestedObject nested) {
            this.nested = nested;
        }
    }
}

package io.github.panjung99.panapi.common.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final DateTimeFormatter RFC3339_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME; // 标准 RFC3339 格式解析器

    /**
     * 把 RFC3339 / ISO8601 格式的时间字符串转为 LocalDateTime。
     * 示例："2025-11-11T15:30:00+08:00" → LocalDateTime(2025-11-11T15:30)
     */
    public static LocalDateTime rfc3399ToLocalDateTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        OffsetDateTime odt = OffsetDateTime.parse(timeStr, RFC3339_FORMATTER);
        return odt.toLocalDateTime();
    }

    /**
     * 将yyyyMMddHHmmss格式的字符串转换为LocalDateTime
     * 示例："20251112123045" → LocalDateTime(2025-11-12T12:30:45)
     *
     * @param timeStr 时间字符串（格式：yyyyMMddHHmmss）
     * @return 对应的LocalDateTime对象，如果格式错误或为空则返回null
     */
    public static LocalDateTime yyyyMMddHHmmssToLocalDateTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(timeStr, FORMATTER);
    }

    /**
     * 解析格式为 "yyyy-MM-dd HH:mm:ss" 的时间字符串
     * 示例："2022-11-22 11:55:42" → LocalDateTime(2022-11-22T11:55:42)
     */
    public static LocalDateTime normalDateToLocalDateTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timeStr, formatter);
    }

    /**
     * 获取yyyyMMddHHmmss格式的xx分钟后的时间
     * @param minutes 当前时间向后多少分钟
     * @return
     */
    public static String getTimeAfterMinutes(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes).format(FORMATTER);
    }

    /**
     * 转换为月初（当月第一天的00:00:00）
     * @param date
     * @return
     */
    public static LocalDateTime getStartOfMonth(LocalDateTime date) {
        LocalDateTime startOfMonth = date.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return startOfMonth;
    }

    /**
     * 转换为月末（当月最后一天的23:59:59.999999999）
     * @param date
     * @return
     */
    public static LocalDateTime getEndOfMonth(LocalDateTime date) {
        LocalDateTime endOfMonth = date
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
        return endOfMonth;
    }

    /**
     * 转换为当天开始时间（00:00:00.000000000）
     * @param date
     * @return
     */
    public static LocalDateTime getStartOfDay(LocalDateTime date) {
        return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 转换为当天结束时间（23:59:59.999999999）
     * @param date
     * @return
     */
    public static LocalDateTime getEndOfDay(LocalDateTime date) {
        return date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }





}

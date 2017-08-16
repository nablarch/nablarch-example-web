package com.nablarch.example.app.web.formatter;

import nablarch.common.web.tag.DateTimeFormatter;
import nablarch.common.web.tag.ValueFormatter;
import nablarch.common.web.tag.YYYYMMDDFormatter;

import javax.servlet.jsp.PageContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * 日付のフォーマットを行うクラス。
 * <p/>
 * フォーマット対象のオブジェクトが{@link Date}型であれば、{@link DateTimeFormatter#format(PageContext, String, Object, String)}、
 * {@link Date}型以外であれば{@link YYYYMMDDFormatter#format(PageContext, String, Object, String)}に処理を委譲する。
 *
 * @author Nabu Rakutaro
 */
public class ExampleDateTimeFormatter implements ValueFormatter {

    /**
     * {@link Date}オブジェクトを日付文字列に変換するフォーマッター
     */
    private ValueFormatter dateTimeFormatter;

    /**
     * 文字列を日付文字列に変換するフォーマッター
     */
    private ValueFormatter yyyymmddFormatter;

    @Override
    public String format(PageContext pageContext, String name, Object value, String pattern) {
        if (value instanceof Date) {
            return dateTimeFormatter.format(pageContext, name, value, pattern);
        } else if (value instanceof ChronoLocalDate) {
            return ((ChronoLocalDate) value).format(java.time.format.DateTimeFormatter.ofPattern(pattern));
        } else {
            return yyyymmddFormatter.format(pageContext, name, value, pattern);
        }
    }

    /**
     * {@link Date}をフォーマットする{@link ValueFormatter}を設定する。
     *
     * @param dateTimeFormatter フォーマッター
     */
    public void setDateTimeFormatter(final ValueFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    /**
     * 文字列形式の日付をフォーマとする{@link ValueFormatter}を設定する。
     *
     * @param yyyymmddFormatter フォーマッター
     */
    public void setYyyymmddFormatter(final ValueFormatter yyyymmddFormatter) {
        this.yyyymmddFormatter = yyyymmddFormatter;
    }
}

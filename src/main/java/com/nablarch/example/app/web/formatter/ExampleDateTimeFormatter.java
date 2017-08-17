package com.nablarch.example.app.web.formatter;

import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.servlet.jsp.PageContext;

import nablarch.common.web.tag.ValueFormatter;
import nablarch.common.web.tag.YYYYMMDDFormatter;

/**
 * 日付のフォーマットを行うクラス。
 * <p>
 * <ul>
 *     <li>{@link Date}型であれば、{@link nablarch.common.web.tag.DateTimeFormatter#format(PageContext, String, Object, String)}でフォーマットを行う。</li>
 *     <li>{@link ChronoLocalDate}であれば{@link DateTimeFormatter}でフォーマットを行う。</li>
 *     <li>上記以外の型であれば{@link YYYYMMDDFormatter#format(PageContext, String, Object, String)}でフォーマットを行う。</li>
 * </ul>
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
            return ((ChronoLocalDate) value).format(DateTimeFormatter.ofPattern(pattern));
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

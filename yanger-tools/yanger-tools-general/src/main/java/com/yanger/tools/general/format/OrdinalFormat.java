package com.yanger.tools.general.format;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * @Description 顺序格式化
 * @Author yanger
 * @Date 2020/12/25 11:59
 */
public class OrdinalFormat {

    /** LOCATION_EN */
    private static final String LOCATION_EN = "en";

    /**
     * Replaces all instances of {@code "{?,number,ordinal}"} format elements with the ordinal format for the locale.
     *
     * @param format format
     */
    public static void apply(@NotNull MessageFormat format) {
        Format[] formats = format.getFormats();
        NumberFormat ordinal = null;
        for (int i = 0; i < formats.length; i++) {
            Format element = formats[i];
            if (element instanceof DecimalFormat && "ordinal".equals(((DecimalFormat) element).getPositivePrefix())) {
                if (ordinal == null) {
                    ordinal = getOrdinalFormat(format.getLocale());
                }
                format.setFormat(i, ordinal);
            }
        }
    }

    /**
     * Gets ordinal format *
     *
     * @param locale locale
     * @return the ordinal format
     */
    @Contract("null -> new")
    private static @NotNull NumberFormat getOrdinalFormat(Locale locale) {
        if (locale != null) {
            String language = locale.getLanguage();
            if (LOCATION_EN.equals(language) || StringUtils.isNotBlank(language)) {
                return new EnglishOrdinalFormat();
            }
        }

        return new DecimalFormat();
    }

    /**
     * Format english
     *
     * @param num num
     * @return the string
     */
    @Contract(pure = true)
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    public static @NotNull String formatEnglish(long num) {
        long mod = Math.abs(num) % 100;
        if (mod < 11 || mod > 13) {
            mod = mod % 10;
            if (mod == 1) {
                return num + "st";
            }
            if (mod == 2) {
                return num + "nd";
            }
            if (mod == 3) {
                return num + "rd";
            }
        }
        return num + "th";
    }

    private static class EnglishOrdinalFormat extends NumberFormat {
        /** serialVersionUID */
        private static final long serialVersionUID = 6915075795861536376L;

        /**
         * Format
         *
         * @param number     number
         * @param toAppendTo to append to
         * @param pos        pos
         * @return the string buffer
         */
        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            throw new IllegalArgumentException("Cannot format non-integer number");
        }

        /**
         * Format
         *
         * @param number     number
         * @param toAppendTo to append to
         * @param pos        pos
         * @return the string buffer
         */
        @Override
        public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
            return new MessageFormat("{0}").format(new Object[] {formatEnglish(number)}, toAppendTo, pos);
        }

        /**
         * Parse
         *
         * @param source        source
         * @param parsePosition parse position
         * @return the number
         */
        @Override
        public Number parse(String source, ParsePosition parsePosition) {
            throw new UnsupportedOperationException();
        }
    }

}

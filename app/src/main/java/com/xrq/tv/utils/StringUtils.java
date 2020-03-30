package com.xrq.tv.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static final String TIME_TYPE_1 = "[1-9][0-9]{3}\\-[0-9]{1,2}\\-[0-9]{1,2}"; //2015-5-12
    public static final String TIME_TYPE_2 = "[0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}"; //15:22:30
    public static final String TIME_TYPE_3 = "[1-9][0-9]{3}\\-[0-9]{1,2}\\-[0-9]{1,2}\\s*[a-zA-Z]*\\s*[0-9]{1,2}\\:[0-9]{1,2}"; //2015-5-12 20:15
    public static final String TIME_TYPE_4 = "[1-9][0-9]{3}\\-[0-9]{1,2}\\-[0-9]{1,2}\\s*[a-zA-Z]*\\s*[0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}";//2015-5-12 20:15:52
    private String styl = TIME_TYPE_1;

    //检测是否为空
    public static <T extends String> boolean checkStringNull(T string) {
        return string != null && !string.isEmpty();
    }

    public static String getString(String string) {
        if (checkStringNull(string)) {
            return string;
        } else {
            return "";
        }
    }

    public static String getString(String string, String defaultString) {
        if (checkStringNull(string)) {
            return string;
        } else {
            return defaultString;
        }
    }

    private StringUtils() {
    }

    public static StringUtils getInstance() {
        return StringUtilsHolder.STRING_UTILS;
    }

    private static class StringUtilsHolder {
        private static final StringUtils STRING_UTILS = new StringUtils();
    }

    public StringUtils setModal(String styl) {
        this.styl = styl;
        return this;
    }

    public String regularization(String strPrimary, String strReplace, String strResult) {
        Pattern p;
        Matcher m = null;
//        CheckUtils.checkNotNull(strPrimary, "StringUtils strPrimary is null");
        if (strPrimary == null) {
            return "";
        }
        if (styl != null && !styl.isEmpty()) {
            p = Pattern.compile(styl);
            m = p.matcher(strPrimary);
        } else {
            return strPrimary;
        }
        if (m.find()) {
            if (strReplace == null || strResult == null) {
                return m.group(0);
            } else {
                return m.group(0).replace(strReplace, strResult);
            }
        } else {
            return strPrimary;
        }
    }
}

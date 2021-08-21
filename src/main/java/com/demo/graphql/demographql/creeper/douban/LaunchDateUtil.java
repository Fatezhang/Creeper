package com.demo.graphql.demographql.creeper.douban;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class LaunchDateUtil {
    public static LocalDate extractDateFromString(String str) {
        var regex = "([0-9]{4})(-[0-9]{2}){0,2}";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(str);
        if (matcher.find()) {
            var dateStr = matcher.group();
            if (dateStr.length() == 4) {
                dateStr += "-01-01";
            }
            if (dateStr.length() == 7) {
                dateStr += "-01";
            }
            return LocalDate.parse(dateStr);
        }
        return null;
    }
}

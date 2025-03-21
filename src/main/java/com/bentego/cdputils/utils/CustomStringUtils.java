package com.bentego.cdputils.utils;

public final class CustomStringUtils {
    public static String removeBaseSuffix(String input) {

        String suffix = "-BASE";
        if (input.endsWith(suffix)) {
            return input.substring(0, input.length() - suffix.length());
        }
        return input;
    }
}

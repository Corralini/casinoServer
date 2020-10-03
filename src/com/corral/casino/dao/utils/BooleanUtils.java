package com.corral.casino.dao.utils;

public class BooleanUtils {

    private BooleanUtils() {
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public static boolean intToBool(int i) {
        boolean bool = true;
        if (i == 0) bool = false;
        return bool;
    }
}

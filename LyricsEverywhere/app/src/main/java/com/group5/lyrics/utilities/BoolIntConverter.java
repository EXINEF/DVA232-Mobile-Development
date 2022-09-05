package com.group5.lyrics.utilities;

public class BoolIntConverter {

    /**
     * Convert an int in a boolean
     *
     * @param num num
     * @return true, false or Exception
     */
    public static boolean getBoolFromInt(int num) {
        if (num == 1)
            return true;
        else if (num == 0)
            return false;
        else
            throw new IllegalArgumentException("Only 1 or 0 are accepted, your value is: "+num);
    }

    /**
     * Convert a boolean in  an int
     *
     * @param bool boolean
     * @return 0 or 1
     */
    public static int getIntFromBool(boolean bool) {
        if (bool)
            return 1;
        else
            return 0;
    }
}

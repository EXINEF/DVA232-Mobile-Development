package com.group5.lyrics.utilities;

import android.content.pm.ApplicationInfo;

public class TrackListening {

    public static String TRACK_INFORMATION = "";
    public static String TRACK_FOUND = "";
    public static String TRACK_COUNTER = "";
    public static boolean IS_CHANGED = false;
    public static String TITLE_FOUND = "";
    public static boolean APP_TRIGGER = false;
    public static String APPLICATION_FOUND = null;

    public static String isSongDetected(boolean b) {
        if(b)
            return "Song Is Detected";
        else
            return "Press the button to search for a song";
    }
}

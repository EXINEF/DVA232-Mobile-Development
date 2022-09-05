package com.group5.lyrics.db;

import android.provider.BaseColumns;

public final class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {}

    /* Inner class that defines the SONGS TABLE */
    public static class SongsEntry implements BaseColumns {
        public static final String TABLE_NAME = "recent_songs";
        public static final String TRACK_ID = "track_id";
        public static final String TRACK_NAME = "track_name";
        public static final String ARTIST_ID = "artist_id";
        public static final String ARTIST_NAME = "artist_name";
        public static final String ALBUM_ID = "album_id";
        public static final String ALBUM_NAME = "album_name";
        public static final String LYRICS_ID = "lyrics_id";
        public static final String LYRICS_CONTENT = "lyrics_content";
        public static final String IS_EXPLICIT = "is_explicit";
        public static final String HAS_LYRICS = "has_lyrics";
        public static final String IS_FAVORITE = "is_favorite";
        public static final String IS_RECENT = "is_recent";
        public static final String CREATE_DATETIME = "create_datetime";
    }

    /* Inner class that defines the SONGS TABLE */
    public static class SettingsEntry implements BaseColumns {

        public static final String TABLE_NAME = "user_settings";
        public static final String ID = "id";
        public static final String SCREEN_ALWAYS_ACTIVE = "screen_always_active";
        public static final String FAVORITE_LYRICS_OFFLINE = "favorite_lyrics_offline";
        public static final String RECENT_LYRICS_OFFLINE = "recent_lyrics_offline";
        public static final String RECEIVE_NOTIFICATION = "receive_notification";
        public static final String MAX_NUM_RECENT_TO_SAVE = "max_num_recent_to_save";
    }

}

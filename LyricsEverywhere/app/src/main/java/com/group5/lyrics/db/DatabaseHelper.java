package com.group5.lyrics.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.group5.lyrics.models.song.Album;
import com.group5.lyrics.models.song.Artist;
import com.group5.lyrics.models.song.Lyrics;
import com.group5.lyrics.models.song.Track;
import com.group5.lyrics.models.song.TrackInterface;
import com.group5.lyrics.models.user.DefaultUserSettings;
import com.group5.lyrics.models.user.UserSettings;
import com.group5.lyrics.models.user.UserSettingsInterface;
import com.group5.lyrics.utilities.BoolIntConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseHelperInterface {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_FOR_SONGS);
            db.execSQL(CREATE_TABLE_FOR_SETTINGS);
        } catch (SQLiteException exception) {
            Log.e("DB ALREADY CREATED: ", exception.getMessage());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DELETE_TABLE_SONGS);
        db.execSQL(DELETE_TABLE_SETTINGS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public List<TrackInterface> getAllTracks(SQLiteDatabase db) {
        List<TrackInterface> tracks = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + DatabaseContract.SongsEntry.TABLE_NAME + " ORDER BY create_datetime DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            TrackInterface track = new Track(
                    cursor.getInt(0),
                    cursor.getString(1),
                    new Artist(cursor.getInt(2), cursor.getString(3)),
                    new Album(cursor.getInt(4), cursor.getString(5)),
                    new Lyrics(cursor.getInt(6), cursor.getString(7)),
                    cursor.getInt(8),
                    cursor.getInt(9),
                    cursor.getInt(10),
                    cursor.getInt(11),
                    cursor.getString(12)
            );

            tracks.add(track);
            cursor.moveToNext();
        }
        cursor.close();
        return tracks;
    }

    @Override
    public TrackInterface getTrackById(SQLiteDatabase db, int id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.SongsEntry.TRACK_ID,
                DatabaseContract.SongsEntry.TRACK_NAME,
                DatabaseContract.SongsEntry.ARTIST_ID,
                DatabaseContract.SongsEntry.ARTIST_NAME,
                DatabaseContract.SongsEntry.ALBUM_ID,
                DatabaseContract.SongsEntry.ALBUM_NAME,
                DatabaseContract.SongsEntry.LYRICS_ID,
                DatabaseContract.SongsEntry.LYRICS_CONTENT,
                DatabaseContract.SongsEntry.IS_EXPLICIT,
                DatabaseContract.SongsEntry.HAS_LYRICS,
                DatabaseContract.SongsEntry.IS_FAVORITE,
                DatabaseContract.SongsEntry.IS_RECENT,
                DatabaseContract.SongsEntry.CREATE_DATETIME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DatabaseContract.SongsEntry.TRACK_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DatabaseContract.SongsEntry.TRACK_ID + " DESC";

        Cursor cursor = db.query(
                DatabaseContract.SongsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder              // The sort order
        );

        TrackInterface song = null;
        while (cursor.moveToNext()) {
            song = new Track(
                    cursor.getInt(0),
                    cursor.getString(1),
                    new Artist(cursor.getInt(2), cursor.getString(3)),
                    new Album(cursor.getInt(4), cursor.getString(5)),
                    new Lyrics(cursor.getInt(6), cursor.getString(7)),
                    cursor.getInt(8),
                    cursor.getInt(9),
                    cursor.getInt(10),
                    cursor.getInt(11)
                    // cursor.getBlob(12),
                    // cursor.getString(13)
            );
        }
        cursor.close();
        return song;
    }

    @Override
    public long addTrack(SQLiteDatabase db, TrackInterface track) {
        TrackInterface checkTrack = getTrackById(db, track.getId());

        if (checkTrack == null) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.SongsEntry.TRACK_ID, track.getId());
            values.put(DatabaseContract.SongsEntry.TRACK_NAME, track.getName());
            values.put(DatabaseContract.SongsEntry.ARTIST_ID, track.getArtist().getId());
            values.put(DatabaseContract.SongsEntry.ARTIST_NAME, track.getArtist().getName());
            values.put(DatabaseContract.SongsEntry.ALBUM_ID, track.getAlbum().getId());
            values.put(DatabaseContract.SongsEntry.ALBUM_NAME, track.getAlbum().getName());
            values.put(DatabaseContract.SongsEntry.LYRICS_ID, track.getLyrics().getId());
            values.put(DatabaseContract.SongsEntry.LYRICS_CONTENT, track.getLyrics().getContent());
            values.put(DatabaseContract.SongsEntry.IS_EXPLICIT, track.isExplicit());
            values.put(DatabaseContract.SongsEntry.HAS_LYRICS, track.hasLyrics());
            values.put(DatabaseContract.SongsEntry.IS_FAVORITE, 0);
            values.put(DatabaseContract.SongsEntry.IS_RECENT, 1);
            values.put(DatabaseContract.SongsEntry.CREATE_DATETIME, getTimeNow().toString()); // TODO add something automatic to store the time
            return db.insert(DatabaseContract.SongsEntry.TABLE_NAME, null, values);
        }

        // track already exist in db
        return -1;
    }

    @Override
    public long removeTrack(SQLiteDatabase db, int id) {
        Cursor cursor = db.rawQuery("DELETE FROM " + DatabaseContract.SongsEntry.TABLE_NAME + " WHERE track_id = " + id, null);
        cursor.close();
        return 0;
    }

    @Override
    public long updateSettings(SQLiteDatabase db, UserSettingsInterface newSettings) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.SettingsEntry.SCREEN_ALWAYS_ACTIVE, newSettings.getScreenAlwaysActive());
        cv.put(DatabaseContract.SettingsEntry.FAVORITE_LYRICS_OFFLINE, newSettings.getFavouriteLyricsOffline());
        cv.put(DatabaseContract.SettingsEntry.RECENT_LYRICS_OFFLINE, newSettings.getRecentLyricsOffline());
        cv.put(DatabaseContract.SettingsEntry.RECEIVE_NOTIFICATION, newSettings.getReceiveNotification());
        cv.put(DatabaseContract.SettingsEntry.MAX_NUM_RECENT_TO_SAVE, newSettings.getMaxRecentToSave());
        db.update(DatabaseContract.SettingsEntry.TABLE_NAME, cv, DatabaseContract.SettingsEntry.ID + "=" + 0, null);
        return 0;
    }

    @Override
    public long addUserSettings(SQLiteDatabase db, UserSettingsInterface userSetting) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.SettingsEntry.ID, 0);
        values.put(DatabaseContract.SettingsEntry.SCREEN_ALWAYS_ACTIVE, userSetting.getScreenAlwaysActive());
        values.put(DatabaseContract.SettingsEntry.FAVORITE_LYRICS_OFFLINE, userSetting.getFavouriteLyricsOffline());
        values.put(DatabaseContract.SettingsEntry.RECENT_LYRICS_OFFLINE, userSetting.getRecentLyricsOffline());
        values.put(DatabaseContract.SettingsEntry.RECEIVE_NOTIFICATION, userSetting.getReceiveNotification());
        values.put(DatabaseContract.SettingsEntry.MAX_NUM_RECENT_TO_SAVE, userSetting.getMaxRecentToSave());
        // Insert the new row, returning the primary key value of the new row
        return db.insert(DatabaseContract.SettingsEntry.TABLE_NAME, null, values);
    }

    @Override
    public UserSettingsInterface getUserSettings(SQLiteDatabase db) {
        UserSettingsInterface userSettings = DefaultUserSettings.getTestSetting();
        String query = "SELECT * FROM " + DatabaseContract.SettingsEntry.TABLE_NAME + " WHERE id = 0";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() < 1) {
            addUserSettings(db, userSettings);
        } else {
            cursor.moveToFirst();
            userSettings = new UserSettings(
                    cursor.getInt(0),
                    BoolIntConverter.getBoolFromInt(cursor.getInt(1)),
                    BoolIntConverter.getBoolFromInt(cursor.getInt(2)),
                    BoolIntConverter.getBoolFromInt(cursor.getInt(3)),
                    BoolIntConverter.getBoolFromInt(cursor.getInt(4)),
                    cursor.getInt(5)
            );
            cursor.close();
        }
        return userSettings;
    }

    @Override
    public void changeFavourite(SQLiteDatabase db, int id, int isFavourite) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.SongsEntry.IS_FAVORITE, isFavourite);
        db.update(DatabaseContract.SongsEntry.TABLE_NAME, cv, DatabaseContract.SongsEntry.TRACK_ID + "=" + id, null);
        db.close(); // Closing database connection
    }

    // TODO finish the table with the right attributes in the right order and types
    private static final String CREATE_TABLE_FOR_SONGS =
            "CREATE TABLE " + DatabaseContract.SongsEntry.TABLE_NAME + " (" +
                    DatabaseContract.SongsEntry.TRACK_ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.SongsEntry.TRACK_NAME + " TEXT," +
                    DatabaseContract.SongsEntry.ARTIST_ID + " INTERGER," +
                    DatabaseContract.SongsEntry.ARTIST_NAME + " TEXT," +
                    DatabaseContract.SongsEntry.ALBUM_ID + " INTERGER," +
                    DatabaseContract.SongsEntry.ALBUM_NAME + " TEXT," +
                    DatabaseContract.SongsEntry.LYRICS_ID + " INTERGER," +
                    DatabaseContract.SongsEntry.LYRICS_CONTENT + " TEXT," +
                    DatabaseContract.SongsEntry.IS_EXPLICIT + " INTERGER," +
                    DatabaseContract.SongsEntry.HAS_LYRICS + " INTERGER," +
                    DatabaseContract.SongsEntry.IS_FAVORITE + " INTERGER," +
                    DatabaseContract.SongsEntry.IS_RECENT + " INTERGER," +
                    DatabaseContract.SongsEntry.CREATE_DATETIME + " DATE)";

    //TODO check if the type are defined in the proper way
    private static final String CREATE_TABLE_FOR_SETTINGS =
            "CREATE TABLE " + DatabaseContract.SettingsEntry.TABLE_NAME + "(" +
                    DatabaseContract.SettingsEntry.ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.SettingsEntry.SCREEN_ALWAYS_ACTIVE + " INTEGER," +
                    DatabaseContract.SettingsEntry.FAVORITE_LYRICS_OFFLINE + " INTEGER," +
                    DatabaseContract.SettingsEntry.RECENT_LYRICS_OFFLINE + " INTEGER," +
                    DatabaseContract.SettingsEntry.RECEIVE_NOTIFICATION + " INTEGER," +
                    DatabaseContract.SettingsEntry.MAX_NUM_RECENT_TO_SAVE + " INTEGER)";

    private static final String DELETE_TABLE_SONGS =
            "DROP TABLE IF EXISTS " + DatabaseContract.SongsEntry.TABLE_NAME;

    private static final String DELETE_TABLE_SETTINGS =
            "DROP TABLE IF EXISTS " + DatabaseContract.SettingsEntry.TABLE_NAME;

    private Date getTimeNow() {
        Calendar c = new GregorianCalendar();
        Log.e("DATE GET: ", c.getTime().toString());
        return c.getTime();

    }


}




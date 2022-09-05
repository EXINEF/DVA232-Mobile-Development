package com.group5.lyrics.db;

import android.database.sqlite.SQLiteDatabase;

import com.group5.lyrics.models.song.TrackInterface;
import com.group5.lyrics.models.user.UserSettingsInterface;

import java.util.List;

public interface DatabaseHelperInterface {

    /**
     * Return all the Tracks in the database by a List of tracks
     *
     * @param db
     * @return
     */
    List<TrackInterface> getAllTracks(SQLiteDatabase db);

    /**
     * Return a track by giving its id or return null if the track does not exist in the database
     *
     * @param db
     * @param id
     * @return
     */
    TrackInterface getTrackById(SQLiteDatabase db, int id);

    /**
     * Add a track to the Database
     *
     * @param track
     * @return row of the track or -1 if not successful
     */
    long addTrack(SQLiteDatabase db, TrackInterface track);

    /**
     * Remove a track from the Database by its Id
     *
     * @param id track id
     * @return row of the removed track or -1 if not successful
     */
    long removeTrack(SQLiteDatabase db, int id);

    /**
     * Add a UserSettings to the Database
     *
     * @param userSettings
     * @return
     */
    long addUserSettings(SQLiteDatabase db, UserSettingsInterface userSettings);

    /**
     * Change Track favourite attribute in the database
     *
     * @param db database
     * @param id track id
     * @param isFavourite new status of favourite
     */
    void changeFavourite(SQLiteDatabase db, int id, int isFavourite);

    /**
     * Return the user settings saved in the database
     *
     * @param db database
     * @return UserSettings
     */
    UserSettingsInterface getUserSettings(SQLiteDatabase db);

    /**
     * Update the UserSettings on the database
     *
     * @param db database
     * @param newSettings new UserSettings
     *
     * @return -1 if error otherwise 0
     */
    long updateSettings(SQLiteDatabase db, UserSettingsInterface newSettings);


}

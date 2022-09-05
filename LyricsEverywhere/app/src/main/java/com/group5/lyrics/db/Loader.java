package com.group5.lyrics.db;

import android.database.sqlite.SQLiteDatabase;

import com.group5.lyrics.models.song.TrackInterface;

import java.util.ArrayList;
import java.util.List;

public class Loader implements LoaderInterface {

    private final DatabaseHelper dbHelper;
    private final List<TrackInterface> tracks;

    public Loader(DatabaseHelper dbHelper) {
        if (dbHelper == null)
            throw new NullPointerException("Database Helper must be NOT NULL");
        this.dbHelper = dbHelper;
        this.tracks = loadAllTracks();
    }

    @Override
    public List<TrackInterface> getAllRecentTracks() {
        List<TrackInterface> recentTracks = new ArrayList<>();
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).isRecent())
                recentTracks.add(tracks.get(i));
        }
        return recentTracks;
    }

    @Override
    public List<TrackInterface> getAllFavouritesTracks() {
        List<TrackInterface> favouritesTracks = new ArrayList<>();
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).isFavourite())
                favouritesTracks.add(tracks.get(i));
        }
        return favouritesTracks;
    }

    @Override
    public List<TrackInterface> getAllTracks() {
        return tracks;
    }

    @Override
    public TrackInterface getTrack(int trackId) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getId() == trackId)
                return tracks.get(i);
        }
        return null;
    }

    @Override
    public boolean changeTrackFavouriteParameter(int id, int isFavourite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.changeFavourite(db, id, isFavourite);
        dbHelper.close();
        return false;
    }

    /**
     * Load all the track from the database in an ArrayList
     *
     * @return tracks' ArrayList
     */
    private List<TrackInterface> loadAllTracks() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return dbHelper.getAllTracks(db);
    }

}

package com.group5.lyrics.db;

import com.group5.lyrics.models.song.TrackInterface;

import java.util.List;

public interface LoaderInterface {

    /**
     * Return all Recent Tracks stored in the database
     *
     * @return recent Tracks
     */
    List<TrackInterface> getAllRecentTracks();

    /**
     * Return all Favourites Tracks stored in the database
     *
     * @return Favourites Tracks
     */
    List<TrackInterface> getAllFavouritesTracks();

    /**
     * Return all tracks stored in the database
     *
     * @return all tracks
     */
    List<TrackInterface> getAllTracks();

    /**
     * Return track by id in the database
     *
     * @return track
     */
    TrackInterface getTrack(int trackId);

    /**
     * Change favourite track status with new one
     *
     * @param id track id
     * @param isFavourite new status of favourite
     * @return
     */
    boolean changeTrackFavouriteParameter(int id, int isFavourite);
}

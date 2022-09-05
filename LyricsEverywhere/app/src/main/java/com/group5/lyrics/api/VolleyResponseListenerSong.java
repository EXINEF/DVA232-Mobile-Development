package com.group5.lyrics.api;

import com.group5.lyrics.models.song.TrackInterface;

import org.json.JSONException;

import java.util.List;

public interface VolleyResponseListenerSong {

    /**
     * Use a ModelViewController Pattern, to handle the request because is not Single Thread
     * so we use an interface to wait for it
     *
     * @param tracks ArrayList of tracks that were found on the API
     *
     * @throws JSONException JSON Exception
     */
    void onResponse(List<TrackInterface> tracks);

    void onError(String message);
}
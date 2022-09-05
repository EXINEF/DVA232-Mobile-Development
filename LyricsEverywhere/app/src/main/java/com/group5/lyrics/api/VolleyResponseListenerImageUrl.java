package com.group5.lyrics.api;

import com.group5.lyrics.models.song.LyricsInterface;

import org.json.JSONException;

public interface VolleyResponseListenerImageUrl {

    /**
     * Use a ModelViewController Pattern, to handle the request because is not Single Thread
     * so we use an interface to wait for it
     *
     * @param url Image url of the album
     *
     * @throws JSONException JSON Exception
     */
    void onResponse(String url) throws JSONException;

    void onError(String message);
}

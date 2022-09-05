package com.group5.lyrics.api;

import com.group5.lyrics.models.song.LyricsInterface;
import org.json.JSONException;

public interface VolleyResponseListenerLyrics {

    /**
     * Use a ModelViewController Pattern, to handle the request because is not Single Thread
     * so we use an interface to wait for it
     *
     * @param lyrics Lyrics of a Track
     *
     * @throws JSONException JSON Exception
     */
    void onResponse(LyricsInterface lyrics) throws JSONException;

    void onError(String message);
}

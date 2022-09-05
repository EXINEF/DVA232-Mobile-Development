package com.group5.lyrics.models.song;

public interface LyricsInterface {

    /**
     * Return the track id associated to the lyric as it is in the API
     *
     * @return track Id
     */
    int getId();

    /**
     * Return Lyric's text as it is in the API
     *
     * @return Lyric's text
     */
    String getContent();
}

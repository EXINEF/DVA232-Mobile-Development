package com.group5.lyrics.models.song;

public interface ArtistInterface {
    /**
     * Return the Artist id associated to a lyric as it is in the API
     *
     * @return Artist Id
     */
    int getId();

    /**
     * Return the Artist name associated to a lyric as it is in the API
     *
     * @return Artist name
     */
    String getName();

}

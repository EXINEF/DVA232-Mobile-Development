package com.group5.lyrics.api;

public interface ApiInterface {

    /**
     * Search a list of songs given a name
     *
     * @param songName Song Name
     * @param vrl Volley Response Listener
     */
    void searchBySongName(String songName, VolleyResponseListenerSong vrl);

    /**
     * Search the lyrics of a song given the id of that song
     *
     * @param trackId Track Id
     * @param vrl Volley Response Listener
     */
    void getLyricsFromSongId(int trackId, VolleyResponseListenerLyrics vrl);

    /**
     * Search the url image of an album giving its id
     *
     * @param albumId Album Id
     * @param vrl Volley Response Listener
     */
    void getUrlImageFromAlbumId(int albumId, VolleyResponseListenerImageUrl vrl);
}

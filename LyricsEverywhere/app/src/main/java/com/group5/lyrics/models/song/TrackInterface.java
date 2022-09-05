package com.group5.lyrics.models.song;

//used to define methods that will be implemented in the Song class
public interface TrackInterface {

    /**
     * Return the track id as it is in the API
     *
     * @return track Id
     */
    int getId();

    /**
     * Return the track name as it is in the API
     *
     * @return track Name
     */
    String getName();

    /**
     * Return's true if the track lyrics are found, false if not
     *
     * @return true/false
     */
    boolean hasLyrics();

    /**
     * Return's true if the track lyrics are explicit, false if not
     *
     * @return true/false
     */
    boolean isExplicit();

    /**
     * Return's true if the track is in the favourites, false if not
     *
     * @return true/false
     */
    boolean isFavourite();

    /**
     * Return's true if the track is in the recent, false if not
     *
     * @return true/false
     */
    boolean isRecent();

    /**
     * Set favourite new status
     *
     * @param value favourite status
     */
    void setFavourite(boolean value);

    /**
     * Set if it's recent or not
     *
     * @param value recent status
     */
    void setRecent(boolean value);

    /**
     * Return the lyrics of the track
     *
     * @return Lyrics
     */
    LyricsInterface getLyrics();

    /**
     * Return the Album of the track
     *
     * @return Album
     */
    AlbumInterface getAlbum();

    /**
     * Return the Artist of the track
     *
     * @return Artist
     */
    ArtistInterface getArtist();

    /**
     * Set the new lyrics for the track
     *
     * @param lyrics new Lyrics
     */
    void setLyrics(LyricsInterface lyrics);

}

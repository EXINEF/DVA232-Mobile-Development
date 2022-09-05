package com.group5.lyrics.models.song;

public class TestTrack {

    static LyricsInterface lyrics = new Lyrics(
            3482,
            "Default Lyrics"
    );

    static ArtistInterface artist = new Artist(
            4252,
            "Default Artist"
    );

    static AlbumInterface album = new Album(
            7858,
            "Default Album"
    );

    static TrackInterface track = new Track(
            79,
            "Default Track Name",
            artist,
            album,
            lyrics,
            1,
            1,
            1,
            1
    );

    public static TrackInterface getTestTrack() {
        return track;
    }
}

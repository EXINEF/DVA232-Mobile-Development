package com.group5.lyrics.api;

import android.content.Context;
import android.util.Log;

public class Api implements ApiInterface{
    //Strings that will be used in the json URL query are defined.
    private static final String DOMAIN = "https://api.musixmatch.com/ws/1.1/";
    private static final String QUERY_FOR_SONG_NAME = "track.search?f_has_lyrics=1&q_track=";
    private static final String QUERY_FOR_LYRICS_BY_SONG_ID = "track.lyrics.get?track_id=";
    private static final String QUERY_FOR_URL_IMAGE_BY_ALBUM_ID = "album.get?album_id=";
    private static final String ACCESS_KEY = "&apikey=d38c8230d9ca1e7ada2459bf96abb8be";
    private final Context context;

    public Api(Context context) {
        this.context = context;
    }

    @Override
    public void searchBySongName(String songName, VolleyResponseListenerSong vrl) {
        //we define the url following Musixmatch documentation guidelines for queries
        String url = DOMAIN + QUERY_FOR_SONG_NAME + songName + ACCESS_KEY;
        Log.e("DEBUG QUERY name url", url);
        QueryRequest.queryApis(url, vrl, context);
    }

    @Override
    public void getLyricsFromSongId(int trackId, VolleyResponseListenerLyrics vrl) {
        //the query below allows to find lyrics of a song since the track id is known
        String url = DOMAIN + QUERY_FOR_LYRICS_BY_SONG_ID + trackId + ACCESS_KEY;
        QueryRequest.queryApisLyrics(url, vrl, context);
    }

    @Override
    public void getUrlImageFromAlbumId(int albumId, VolleyResponseListenerImageUrl vrl) {
        //the query below allows to find lyrics of a song since the track id is known
        String url = DOMAIN + QUERY_FOR_URL_IMAGE_BY_ALBUM_ID + albumId + ACCESS_KEY;
        QueryRequest.queryApisImageUrl(url, vrl, context);
    }


}

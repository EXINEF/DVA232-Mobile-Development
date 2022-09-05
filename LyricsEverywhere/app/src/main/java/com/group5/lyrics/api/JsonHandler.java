package com.group5.lyrics.api;

import android.util.Log;

import com.group5.lyrics.models.song.Album;
import com.group5.lyrics.models.song.Artist;
import com.group5.lyrics.models.song.Lyrics;
import com.group5.lyrics.models.song.LyricsInterface;
import com.group5.lyrics.models.song.Track;
import com.group5.lyrics.models.song.TrackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHandler {
    /**
     * Create a songs array from the json received by the database
     *
     * @param response
     * @return
     * @throws JSONException
     */

    public static List<TrackInterface> getSongFromJSON(JSONObject response) throws JSONException {
        //Log.e("DEBUG JSON response", response.toString());
        List<TrackInterface> songs = new ArrayList<>();
        JSONObject message = response.getJSONObject("message");
        JSONObject body = message.getJSONObject("body");
        //getting the content of the body as an array, it's the list of songs(tracks)
        JSONArray track_list = body.getJSONArray("track_list");

        for (int i = 0; i < track_list.length(); i++) {
            //printing the song's number in list
            //Log.e("SONG n ", String.valueOf(i));
            //getting the JSON object that contains the track "i" as another JSON object
            JSONObject trackPart = track_list.getJSONObject(i);
            //getting the JSON object track of the trackpart "i"
            JSONObject track = trackPart.getJSONObject("track");
            //check if the track has a lyric through the has lyrics attribute
            boolean has_lyrics = getBoolFromInt((int) track.get("has_lyrics"));

            if (!has_lyrics) {
                //Log.e("SONG HAS NO LYRICS", "");
                break;
            } else {
                //Log.e("SONG HAS LYRICS", "");
                //Defining a new song with the attributes taken from the JSON object
                TrackInterface song = new Track(
                        (int) track.get("track_id"),
                        (String) track.get("track_name"),
                        new Artist((int) track.get("artist_id"), (String) track.get("artist_name")),
                        new Album((int) track.get("album_id"), (String) track.get("album_name")),
                        null, // TODO add the lyrics
                        (int) track.get("explicit"),
                        (int) track.get("has_lyrics")
                );
                songs.add(song);
            }
        }
        //Log.e("\nDEBUG SONGS QUERIED:\n", songs.toString());
        return songs;
    }

    public static LyricsInterface getLyricsFromJson(JSONObject response) throws JSONException {
        //Log.e("DEBUG JSON response", response.toString());
        JSONObject message = response.getJSONObject("message");
        JSONObject body = message.getJSONObject("body");
        JSONObject lyrics = body.getJSONObject("lyrics");

        return new Lyrics(
                (int) lyrics.get("lyrics_id"),
                (String) lyrics.get("lyrics_body"));
    }

    public static String getImageUrlFromJson(JSONObject response) throws JSONException {
        Log.e("DEBUG JSON response", response.toString());
        JSONObject message = response.getJSONObject("message");
        JSONObject body = message.getJSONObject("body");
        JSONObject lyrics = body.getJSONObject("album");
        return (String) lyrics.get("album_coverart_100x100");
    }

    /**
     * TODO
     *
     * @param num
     * @return
     */
    private static boolean getBoolFromInt(int num) {
        if (num == 1)
            return true;
        else if (num == 0)
            return false;
        else
            throw new IllegalArgumentException("Only 1 or 0 are accepted");
    }
}

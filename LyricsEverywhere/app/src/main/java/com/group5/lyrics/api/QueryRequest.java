package com.group5.lyrics.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.group5.lyrics.db.Loader;

import org.json.JSONException;

public class QueryRequest {

    public static void queryApis(String query, VolleyResponseListenerSong vrl, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                query,
                null,
                response -> {
                    try {
                        vrl.onResponse(JsonHandler.getSongFromJSON(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> vrl.onError(error.toString()));
        requestQueue.add(request);
        requestQueue.start();
    }

    public static void queryApisLyrics(String query, VolleyResponseListenerLyrics vrl, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                query,
                null,
                response -> {
                    try {
                        vrl.onResponse(JsonHandler.getLyricsFromJson(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> vrl.onError(error.toString()));
        requestQueue.add(request);
        requestQueue.start();
    }

    public static void queryApisImageUrl(String query, VolleyResponseListenerImageUrl vrl, Context context){
        Log.e("AHHH", query);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                query,
                null,
                response -> {
                    try {
                        vrl.onResponse(JsonHandler.getImageUrlFromJson(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> vrl.onError(error.toString()));
        requestQueue.add(request);
        requestQueue.start();
    }
}

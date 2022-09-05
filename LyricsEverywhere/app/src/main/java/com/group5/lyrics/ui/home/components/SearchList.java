package com.group5.lyrics.ui.home.components;


import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.group5.lyrics.ui.lyrics.LyricsPage;
import com.group5.lyrics.R;
import com.group5.lyrics.api.Api;
import com.group5.lyrics.api.ApiInterface;
import com.group5.lyrics.api.VolleyResponseListenerLyrics;
import com.group5.lyrics.api.VolleyResponseListenerSong;
import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.db.Loader;
import com.group5.lyrics.db.LoaderInterface;
import com.group5.lyrics.models.song.LyricsInterface;
import com.group5.lyrics.models.song.TrackInterface;

import org.json.JSONException;

import java.util.List;

public class SearchList extends Fragment {
    String query;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    View root;

    public SearchList(String query) {
        this.query = query;
    }

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_searchlist, container, false);
        dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();
        LoaderInterface loader = new Loader(dbHelper);
        displayAllTracks(root, loader);
        return root;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void displayAllTracks(View root, LoaderInterface loader) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = root.findViewById(R.id.Allsong);

        ApiInterface api = new Api(getContext());
        api.searchBySongName(query, new VolleyResponseListenerSong() {
            @Override
            public void onResponse(List<TrackInterface> tracks) {
                if (tracks.isEmpty()) {
                    Toast.makeText(getContext(), "No Tracks Found with this name, try again...", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 1; i < tracks.size(); i++) {
                        createAndDisplayTracks(params, linearLayout, tracks.get(i));
                    }
                }
            }

            @Override
            public void onError(String message) {
                Log.d("ERRORS: ", message);
            }
        });
    }

    private void createAndDisplayTracks(RelativeLayout.LayoutParams params, LinearLayout LayoutSongs, TrackInterface track) {
        Log.d("QUERIED SONGS : ", track.getName());
        RelativeLayout newTrack = new RelativeLayout(this.getContext());
        newTrack.setLayoutParams(params);
        TextView text = new TextView(this.getContext());
        text.setId(View.generateViewId());
        text.setText(track.getName());
        ImageButton img = new ImageButton(this.getContext());
        params.setMargins(10, 10, 10, 0);
        img.setBackgroundResource(R.drawable.cavalcade);
        img.setLayoutParams(params);
        img.setLayoutParams(new AbsListView.LayoutParams(190, 180));
        params = new RelativeLayout.LayoutParams(1000, 60);
        params.setMargins(190, 10, 10, 10);
        newTrack.addView(img);
        text.setLayoutParams(params);
        text.setTypeface(null, Typeface.BOLD_ITALIC);
        newTrack.addView(text);
        params = new RelativeLayout.LayoutParams(1000, 60);
        params.setMargins(190, 50, 10, 10);
        TextView tAuthor = new TextView(this.getContext());
        //TODO add right album picture
        tAuthor.setText("Author:" + track.getArtist().getName());
        tAuthor.setLayoutParams(params);
        newTrack.addView(tAuthor);
        LayoutSongs.addView(newTrack);
        //per il bordo alla textview
        GradientDrawable border = new GradientDrawable();
        border.setStroke(2, 0xFF000000); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            newTrack.setBackgroundDrawable(border);
        } else {
            newTrack.setBackground(border);
        }

        if (newTrack.getParent() != null) {
            ((ViewGroup) newTrack.getParent()).removeView(newTrack);
        }

        newTrack.setOnClickListener(v -> {
            ApiInterface api = new Api(getContext());
            api.getLyricsFromSongId(track.getId(), new VolleyResponseListenerLyrics() {
                @Override
                public void onResponse(LyricsInterface lyrics) throws JSONException {
                    track.setLyrics(lyrics);
                    FrameLayout fl = getActivity().findViewById(R.id.homePage);
                    fl.removeAllViews();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                            .replace(R.id.homePage, new LyricsPage(track), "CFragment")
                            .addToBackStack("A_TO_C").commit();
                }


                @Override
                public void onError(String message) {
                    Toast.makeText(getContext(), "ERROR WHILE SEARCHING ITS LYRICS", Toast.LENGTH_SHORT).show();
                }
            });


        });

        LayoutSongs.addView(newTrack);


    }


}

package com.group5.lyrics.ui.home;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group5.lyrics.MainActivity;
import com.group5.lyrics.R;
import com.group5.lyrics.Recents;
import com.group5.lyrics.ScrollingActivity;
import com.group5.lyrics.TrackListening;
import com.group5.lyrics.api.Api;
import com.group5.lyrics.api.VolleyResponseListenerLyrics;
import com.group5.lyrics.api.VolleyResponseListenerSong;
import com.group5.lyrics.databinding.FragmentHomeBinding;
import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.db.Loader;
import com.group5.lyrics.db.LoaderInterface;
import com.group5.lyrics.models.song.LyricsInterface;
import com.group5.lyrics.models.song.TestTrack;
import com.group5.lyrics.models.song.TrackInterface;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    /**
     * This view is called every time we tap the button Home in the bottom left
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView track_listening = root.findViewById(R.id.nowPlayingText);
        track_listening.setText(TrackListening.TRACK_INFORMATION);
        setHasOptionsMenu(true);
        LinearLayout trackListening = root.findViewById(R.id.trackResult);
        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.addTrack(db, TestTrack.getTestTrack());
        LoaderInterface loader = new Loader(dbHelper);
        displayAllHomeTracks(root, loader);
        track_listening.setText(TrackListening.isSongDetected(false));

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
            if (isNotificationServiceEnabled()) {
                //track_listening.setText(TrackListening.TRACK_LISTENING);
                if (TrackListening.IS_CHANGED && !TrackListening.TRACK_COUNTER.equals(TrackListening.TRACK_FOUND)) {
                    Api api = new Api(getContext());
                    api.searchBySongName(TrackListening.TRACK_FOUND, new VolleyResponseListenerSong() {
                        @Override
                        public void onResponse(List<TrackInterface> songs) {
                            if (songs.isEmpty()) {
                                TrackListening.TRACK_INFORMATION = "No Lyrics Found for: " + TrackListening.TRACK_FOUND;
                            } else {
                                TrackListening.TRACK_COUNTER = TrackListening.TRACK_FOUND;
                                TrackListening.TRACK_INFORMATION = songs.get(0).getName() + "\t\n" + songs.get(0).getArtist().getName() + "\t" + songs.get(0).getAlbum().getName() + "\t";

                                track_listening.setOnClickListener(v -> {
                                    Api api = new Api(getContext());
                                    api.getLyricsFromSongId(songs.get(0).getId(), new VolleyResponseListenerLyrics() {
                                        @Override
                                        public void onResponse(LyricsInterface lyrics) {
                                            songs.get(0).setLyrics(lyrics);
                                            dbHelper.addTrack(db, songs.get(0));
                                            Intent activity = new Intent(v.getContext(), ScrollingActivity.class);
                                            activity.putExtra("track_id", songs.get(0).getId());
                                            startActivity(activity);
                                        }
                                        @Override
                                        public void onError(String message) {
                                            Toast.makeText(getContext(), "Impossible to reach the APIs server...", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                });
                            }
                            track_listening.setText(TrackListening.TRACK_INFORMATION);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Error querying the APIs", Toast.LENGTH_SHORT).show();
                            Log.e("QUERY API ERROR:", message);
                        }
                    });
                }

                else if (TrackListening.IS_CHANGED && TrackListening.TRACK_COUNTER.equals(TrackListening.TRACK_FOUND))
                    return;
                else
                    track_listening.setText("Start to listen to a song for seeing lyrics...");


            }
            else
                track_listening.setText("Please Enable Notification Access");
        });


        LinearLayout brecents = root.findViewById(R.id.RecentButton);
        brecents.setOnClickListener(v -> {
            replaceFragment(this);

        });

        LinearLayout bfavourite = root.findViewById(R.id.FavoritesButton);
        bfavourite.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Recents.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search");

        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Api api = new Api(getContext());
                api.searchBySongName(query, new VolleyResponseListenerSong() {
                    @Override
                    public void onResponse(List<TrackInterface> songs) {
                        Log.d("QUERIED SONGS: ", songs.toString());
                    }

                    @Override
                    public void onError(String message) {
                        Log.d("ERRORS: ", message);
                    }
                });
                Toast.makeText(getContext(), "Typing", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void displayAllHomeTracks(View root, LoaderInterface loader) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutRecents = root.findViewById(R.id.recentAll);
        LinearLayout linearLayoutFavourites = root.findViewById(R.id.favoritesAll);

        if (loader.getAllRecentsTracks().isEmpty()) {
            createGridTrackAndDisplay(params, linearLayoutRecents, "NO RECENT TRACKS");
        } else {
            for (int i = 0; i < loader.getAllRecentsTracks().size(); i++) {
                createGridTrackAndDisplay(params,  linearLayoutRecents, loader.getAllRecentsTracks().get(i).getName(), loader.getAllRecentsTracks().get(i).getId());
            }
        }

        if (loader.getAllFavouritesTracks().isEmpty()) {
            createGridTrackAndDisplay(params, linearLayoutFavourites, "NOT FAVOURITES TRACKS");
        } else {
            for (int i = 0; i < loader.getAllFavouritesTracks().size(); i++) {
                createGridTrackAndDisplay(params, linearLayoutFavourites, loader.getAllFavouritesTracks().get(i).getName(), loader.getAllFavouritesTracks().get(i).getId());
            }
        }
    }

    private void createGridTrackAndDisplay(RelativeLayout.LayoutParams params, LinearLayout gridLayoutRecents, String s) {
        RelativeLayout newTrack = new RelativeLayout(this.getContext());
        newTrack.setLayoutParams(params);
        TextView text = new TextView(this.getContext());
        text.setId(View.generateViewId());
        text.setText(s);
        gridLayoutRecents.addView(newTrack);
    }

    private void createGridTrackAndDisplay(RelativeLayout.LayoutParams params, LinearLayout gridLayoutRecents, String s, int id) {

        RelativeLayout newTrack = new RelativeLayout(this.getContext());
        newTrack.setLayoutParams(params);
        TextView text = new TextView(this.getContext());
        text.setId(View.generateViewId());
        text.setText(s);
        ImageButton img = new ImageButton(this.getContext());
        params.setMargins(10, 10, 10, 10);
        img.setBackgroundResource(R.drawable.cavalcade);
        img.setLayoutParams(params);
        img.setLayoutParams(new AbsListView.LayoutParams(300, 300));
        params = new RelativeLayout.LayoutParams(300, 300);
        params.setMargins(0, 300, 10, 10);
        text.setLayoutParams(params);
        newTrack.addView(text);
        newTrack.addView(img);

        img.setOnClickListener(v -> {
            Intent activity = new Intent(v.getContext(), ScrollingActivity.class);
            activity.putExtra("track_id", id);
            startActivity(activity);
        });
        gridLayoutRecents.addView(newTrack);
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getContext().getPackageName();
        final String allNames = Settings.Secure.getString(getContext().getContentResolver(), "enabled_notification_listeners");
        if (allNames != null && !allNames.isEmpty()) {
            for (String name : allNames.split(":")) {
                if (getContext().getPackageName().equals(ComponentName.unflattenFromString(name).getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.homePage, new Recents());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
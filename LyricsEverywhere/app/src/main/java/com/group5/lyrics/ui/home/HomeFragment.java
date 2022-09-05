package com.group5.lyrics.ui.home;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.group5.lyrics.ui.home.components.Favourites;
import com.group5.lyrics.ui.lyrics.LyricsPage;
import com.group5.lyrics.MainActivity;
import com.group5.lyrics.R;
import com.group5.lyrics.ui.home.components.Recents;
import com.group5.lyrics.ui.home.components.SearchList;
import com.group5.lyrics.api.Api;
import com.group5.lyrics.api.ApiInterface;
import com.group5.lyrics.api.VolleyResponseListenerLyrics;
import com.group5.lyrics.api.VolleyResponseListenerSong;
import com.group5.lyrics.databinding.FragmentHomeBinding;
import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.db.Loader;
import com.group5.lyrics.db.LoaderInterface;
import com.group5.lyrics.models.song.LyricsInterface;
import com.group5.lyrics.models.song.TrackInterface;
import com.group5.lyrics.models.user.UserSettingsInterface;
import com.group5.lyrics.utilities.TrackListening;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private int counter = 0;
    private UserSettingsInterface userSettings;
    private TextView recentText;
    private TextView favText;

    /**
     * This view is called every time we tap the button Home in the bottom left
     *
     * @param inflater           LayoutInflater
     * @param container          container
     * @param savedInstanceState savedInstanceState
     * @return view
     */
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView track_listening = root.findViewById(R.id.nowPlayingText);
        track_listening.setText(TrackListening.TRACK_INFORMATION);
        setHasOptionsMenu(true);
        recentText = root.findViewById(R.id.recentText);
        favText = root.findViewById(R.id.favText);

        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        userSettings = dbHelper.getUserSettings(db);
        LoaderInterface loader = new Loader(dbHelper);
        displayAllHomeTracks(root, loader);
        track_listening.setText(TrackListening.isSongDetected(false));

        ImageButton nowPlayingButton = binding.reloadButton;
        nowPlayingButton.setOnClickListener(view -> {
            if (isNotificationServiceEnabled()) {

                if (TrackListening.IS_CHANGED && !TrackListening.TRACK_COUNTER.equals(TrackListening.TRACK_FOUND)) {
                    ApiInterface api = new Api(getContext());
                    api.searchBySongName(TrackListening.TRACK_FOUND, new VolleyResponseListenerSong() {
                        @Override
                        public void onResponse(List<TrackInterface> tracks) {
                            if (tracks.isEmpty()) {
                                TrackListening.TRACK_INFORMATION = "No Lyrics Found for: " + TrackListening.TRACK_FOUND;
                                track_listening.setOnClickListener(v -> {
                                });
                            } else {
                                TrackListening.TRACK_COUNTER = TrackListening.TRACK_FOUND;
                                TrackListening.TRACK_INFORMATION = tracks.get(0).getName() + "\t\n" + tracks.get(0).getArtist().getName() + "\t" + tracks.get(0).getAlbum().getName() + "\t";

                                track_listening.setOnClickListener(v -> {
                                    ApiInterface api = new Api(getContext());
                                    api.getLyricsFromSongId(tracks.get(0).getId(), new VolleyResponseListenerLyrics() {
                                        @Override
                                        public void onResponse(LyricsInterface lyrics) {
                                            tracks.get(0).setLyrics(lyrics);
                                            switchFragmentLyrics(tracks.get(0));
                                        }

                                        @Override
                                        public void onError(String message) {
                                            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                });
                            }
                            track_listening.setText(TrackListening.TRACK_INFORMATION);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            Log.e("QUERY API ERROR:", message);
                        }
                    });
                } else
                    track_listening.setText("Start to listen to a song for seeing lyrics...");


            } else
                track_listening.setText("Please Enable Notification Access");
        });

        if (userSettings.getRecentLyricsOffline() && !loader.getAllRecentTracks().isEmpty()) {
            RelativeLayout buttonRecent = root.findViewById(R.id.RecentButton);
            buttonRecent.setOnClickListener(v -> switchFragment("DFragment", new Recents(), "A_TO_D"));
        }

        if (userSettings.getFavouriteLyricsOffline() && !loader.getAllFavouritesTracks().isEmpty()) {
            RelativeLayout buttonFavourites = root.findViewById(R.id.FavoritesButton);
            buttonFavourites.setOnClickListener(v -> switchFragment("BFragment", new Favourites(), "A_TO_B"));
        }

        return root;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (counter < 1) {
            inflater.inflate(R.menu.menu, menu);
            SearchManager searchManager =(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            MenuItem item = menu.findItem(R.id.action_search);
            androidx.appcompat.widget.SearchView searchBarInput = (androidx.appcompat.widget.SearchView) item.getActionView();
            searchBarInput.setQueryHint("Search");
            item.setActionView(searchBarInput);
            searchBarInput.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String q) {
                    switchFragment("EFragment", new SearchList(q), "A_TO_E");
                    return false;
                }

                //TODO implement search on every time you type
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            counter++;
        }

    }

    public void switchFragment(String tag, Fragment fragment, String name) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.homePage, fragment, tag)
                .addToBackStack(name).commit();

    }

    public void switchFragmentLyrics(TrackInterface track) {
        FrameLayout fl = getActivity().findViewById(R.id.homePage);
        fl.removeAllViews();
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.homePage, new LyricsPage(track), "CFragment")
                .addToBackStack("A_TO_C").commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Load Recent and Favourite Layout in the home Page
     *
     * @param root   view
     * @param loader Loader
     */
    @SuppressLint("SetTextI18n")
    void displayAllHomeTracks(View root, LoaderInterface loader) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutRecent = root.findViewById(R.id.recentAll);
        LinearLayout linearLayoutFavourites = root.findViewById(R.id.favoritesAll);

        if (loader.getAllRecentTracks().isEmpty()) {
            recentText.setText("NO RECENT TRACKS");
        } else if (!userSettings.getRecentLyricsOffline()) {
            recentText.setText("RECENT (enable in settings)");
        } else {

            for (int i = 0; i < loader.getAllRecentTracks().size(); i++) {
                if (i <= userSettings.getMaxRecentToSave() - 1)
                    createGridTrackAndDisplay(params, linearLayoutRecent, loader.getAllRecentTracks().get(i));
            }
        }

        if (loader.getAllFavouritesTracks().isEmpty()) {
            favText.setText("NO FAVOURITE TRACKS");
        } else if (!userSettings.getFavouriteLyricsOffline()) {
            favText.setText("FAVOURITE (enable in settings)");
        } else {
            for (int i = 0; i < loader.getAllFavouritesTracks().size(); i++) {
                createGridTrackAndDisplay(params, linearLayoutFavourites, loader.getAllFavouritesTracks().get(i));
            }
        }
    }

    /**
     * Draw a song in a Layout
     *
     * @param params             parameters
     * @param linearLayoutRecent layout
     * @param track              track to draw
     */
    private void createGridTrackAndDisplay(RelativeLayout.LayoutParams params, LinearLayout linearLayoutRecent, TrackInterface track) {
        RelativeLayout newTrack = new RelativeLayout(this.getContext());
        newTrack.setLayoutParams(params);
        TextView text = new TextView(this.getContext());
        text.setId(View.generateViewId());
        text.setText(track.getName());
        ImageView img = new ImageView(this.getContext());
        params.setMargins(10, 10, 10, 10);
        img.setBackgroundResource(R.drawable.cavalcade);
        img.setLayoutParams(params);
        img.setLayoutParams(new AbsListView.LayoutParams(260, 260));
        params = new RelativeLayout.LayoutParams(260, 260);
        params.setMargins(0, 260, 10, 10);
        text.setLayoutParams(params);
        newTrack.addView(text);
        newTrack.addView(img);

        img.setOnClickListener(v -> switchFragmentLyrics(track));
        linearLayoutRecent.addView(newTrack);
    }


    /**
     * Check if Notification Permission is enabled,
     * otherwise if will open the Android settings to allow notifications on this app
     *
     * @return true or false
     */
    private boolean isNotificationServiceEnabled() {
        final String allNames = Settings.Secure.getString(getContext().getContentResolver(), "enabled_notification_listeners");
        if (allNames != null && !allNames.isEmpty()) {
            for (String name : allNames.split(":")) {
                if (getContext().getPackageName().equals(ComponentName.unflattenFromString(name).getPackageName())) {
                    return true;
                }
            }
        }
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
        return false;
    }

}

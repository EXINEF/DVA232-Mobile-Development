package com.group5.lyrics.ui.lyrics;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.group5.lyrics.R;
import com.group5.lyrics.databinding.FragmentLyricsPageBinding;
import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.db.Loader;
import com.group5.lyrics.db.LoaderInterface;
import com.group5.lyrics.models.song.TrackInterface;
import com.group5.lyrics.models.user.UserSettingsInterface;
import com.group5.lyrics.ui.home.HomeFragment;
import com.group5.lyrics.utilities.BoolIntConverter;

public class LyricsPage extends Fragment {
    TrackInterface track;
    View root;

    public LyricsPage(TrackInterface track) {
        this.track = track;
    }

    private FragmentLyricsPageBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLyricsPageBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        Toolbar toolbar = binding.toolbar;
        setHasOptionsMenu(true);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        TextView t = binding.getRoot().findViewById(R.id.lyrics);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = dbHelper.addTrack(db, track);
        Log.e("INSERT STATUS:", String.valueOf(result));
        LoaderInterface loader = new Loader(dbHelper);


        UserSettingsInterface userSettings = dbHelper.getUserSettings(db);
        if (userSettings.getScreenAlwaysActive()) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (track == null) {
            t.setText("SERIOUS ERROR");
            return null;
        }
        t.setText(track.getLyrics().getContent());
        TextView details=root.findViewById(R.id.artistAlbum);
        details.setText("Artist: " + track.getArtist().getName() + " Album: " + track.getAlbum().getName());
        toolBarLayout.setTitle(track.getName());


        FloatingActionButton favouriteButton = binding.favouriteButton;
        if (track.isFavourite())
            favouriteButton.setImageResource(R.drawable.ic_baseline_heart_broken_24);
        else
            favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);

        favouriteButton.setOnClickListener(v -> {
            if (track.isFavourite()) {
                track.setFavourite(false);
            } else {
                track.setFavourite(true);
            }

            loader.changeTrackFavouriteParameter(track.getId(), BoolIntConverter.getIntFromBool(track.isFavourite()));
            LyricsPage fragment = null;
            fragment = new LyricsPage(track);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FrameLayout fl = getActivity().findViewById(R.id.lyricsPage);
            fl.removeAllViews();
            fragmentTransaction.replace(R.id.lyricsPage, fragment);
            fragmentTransaction.commit();
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                HomeFragment fragment = null;
                fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FrameLayout fl = getActivity().findViewById(R.id.lyricsPage);
                fl.removeAllViews();
                fragmentTransaction.replace(R.id.lyricsPage, fragment);
                fragmentTransaction.commit();


                return true;

            }

            return false;
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
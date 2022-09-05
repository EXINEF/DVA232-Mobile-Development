package com.group5.lyrics.ui.home.components;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.group5.lyrics.ui.lyrics.LyricsPage;
import com.group5.lyrics.R;
import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.db.Loader;
import com.group5.lyrics.db.LoaderInterface;
import com.group5.lyrics.models.song.TrackInterface;
import com.group5.lyrics.ui.home.HomeFragment;

public class Recents extends Fragment {
    View root;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recents, container, false);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.addTrack(db, TestTrack.getTestTrack());
        LoaderInterface loader = new Loader(dbHelper);
        displayAllRecents(root, loader);
        return root;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void displayAllRecents(View root, LoaderInterface loader) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutRecents = root.findViewById(R.id.recentAll1);

        if (loader.getAllRecentTracks().isEmpty()) {
        } else {
            for (int i = 0; i < loader.getAllRecentTracks().size(); i++) {
                createGridTrackAndDisplayRecents(params, linearLayoutRecents, loader.getAllRecentTracks().get(i));
            }
        }

    }

    private void createGridTrackAndDisplayRecents(RelativeLayout.LayoutParams params, LinearLayout linearLayoutRecents, TrackInterface track) {
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
        TextView t = new TextView(this.getContext());
        //TODO AGGIUNGERE NOME AUTORE E FOTO DELL'ALBUM GIUSTA
        t.setText("Author:" + track.getArtist().getName());
        t.setLayoutParams(params);
        newTrack.addView(t);
        linearLayoutRecents.addView(newTrack);
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
            FrameLayout fl = getActivity().findViewById(R.id.homePage);
            fl.removeAllViews();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePage, new LyricsPage(track), "DFragment").commit();
        });

        linearLayoutRecents.addView(newTrack);
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    HomeFragment fragment = null;
                    fragment = new HomeFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FrameLayout fl = getActivity().findViewById(R.id.recentsFragment);
                    fl.removeAllViews();
                    fragmentTransaction.replace(R.id.recentsFragment, fragment);
                    fragmentTransaction.commit();


                    return true;

                }

                return false;
            }
        });
    }

}

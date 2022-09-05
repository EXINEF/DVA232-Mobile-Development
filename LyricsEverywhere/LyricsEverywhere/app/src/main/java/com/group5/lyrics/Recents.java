package com.group5.lyrics;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.db.Loader;
import com.group5.lyrics.db.LoaderInterface;

public class Recents extends Fragment {

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root2 = inflater.inflate(R.layout.activity_recents, container, false);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.addTrack(db, TestTrack.getTestTrack());
        LoaderInterface loader = new Loader(dbHelper);
        displayAllRecents(root2, loader);
        return root2;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void displayAllRecents(View root, LoaderInterface loader) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutRecents = root.findViewById(R.id.recentAll1);

        if (loader.getAllRecentsTracks().isEmpty()) {
        } else {
            for (int i = 0; i < loader.getAllRecentsTracks().size(); i++) {
                createGridTrackAndDisplayRecents(params, linearLayoutRecents, loader.getAllRecentsTracks().get(i).getName(), loader.getAllRecentsTracks().get(i).getId());
            }
        }




        /*  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutRecents = root.findViewById(R.id.recentAll1);
        if (loader.getAllRecentsTracks().isEmpty()) {
        } else {
            for (int i = 0; i < loader.getAllRecentsTracks().size(); i++) {
                createGridTrackAndDisplayRecents(params, linearLayoutRecents, loader.getAllRecentsTracks().get(i).getName(), loader.getAllRecentsTracks().get(i).getId());
            }
        }*/
    }

    private void createGridTrackAndDisplayRecents(RelativeLayout.LayoutParams params, LinearLayout gridLayoutRecents, String s, int id) {
        RelativeLayout newTrack = new RelativeLayout(this.getContext());
        newTrack.setLayoutParams(params);
        TextView text = new TextView(this.getContext());
        text.setId(View.generateViewId());
        text.setText(s);
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
        t.setText("Author:");
        t.setLayoutParams(params);
        newTrack.addView(t);
        gridLayoutRecents.addView(newTrack);
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

        gridLayoutRecents.addView(newTrack);
    }

}
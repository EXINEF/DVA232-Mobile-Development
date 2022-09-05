package com.group5.lyrics.ui.dashboard;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.group5.lyrics.R;
import com.group5.lyrics.databinding.FragmentDashboardBinding;
import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.models.user.UserSettings;
import com.group5.lyrics.models.user.UserSettingsInterface;

import java.util.Arrays;

public class SettingsFragment extends Fragment {


    ListView listView;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DatabaseHelper DbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = DbHelper.getWritableDatabase();
        UserSettingsInterface userSettings = DbHelper.getUserSettings(db);
        Log.e("LOADED SETTINGS:",userSettings.toString());

        Switch screenAlwaysActive = root.findViewById(R.id.screenAlwaysActive);
        Switch favouritesOffline = root.findViewById(R.id.favouritesOffline);
        Switch recentOffline = root.findViewById(R.id.recentOffline);
        Switch receiveNotification = root.findViewById(R.id.receiveNotification);

        screenAlwaysActive.setChecked(userSettings.getScreenAlwaysActive());
        favouritesOffline.setChecked(userSettings.getFavouriteLyricsOffline());
        recentOffline.setChecked(userSettings.getRecentLyricsOffline());
        receiveNotification.setChecked(userSettings.getReceiveNotification());

        EditText recentMaxNumber = root.findViewById(R.id.maxRecentNumber);
        recentMaxNumber.setText(String.valueOf(userSettings.getMaxRecentToSave()));

        Button saveChanges = root.findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(v -> {
            UserSettingsInterface newSettings = new UserSettings(
                    0,
                    screenAlwaysActive.isChecked(),
                    favouritesOffline.isChecked(),
                    recentOffline.isChecked(),
                    receiveNotification.isChecked(),
                    Integer.parseInt(recentMaxNumber.getText().toString())
            );
            DbHelper.updateSettings(db,newSettings);
            Toast.makeText(getContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
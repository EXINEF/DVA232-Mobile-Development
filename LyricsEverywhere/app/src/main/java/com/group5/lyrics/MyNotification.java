package com.group5.lyrics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import com.group5.lyrics.db.DatabaseHelper;
import com.group5.lyrics.models.user.UserSettingsInterface;
import com.group5.lyrics.notifications.NotificationCreation;
import com.group5.lyrics.utilities.TrackListening;

public class MyNotification extends NotificationListenerService {
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // We can read notification while posted.
        for (StatusBarNotification sbm : MyNotification.this.getActiveNotifications()) {
            Bundle b = sbm.getNotification().extras;
            String c = b.toString();
            if(c.contains("android.title")){
                Log.e("MER: ", b.toString());
                if(!TrackListening.APP_TRIGGER) {
                    ApplicationInfo i = (ApplicationInfo) b.get("android.appInfo");
                    TrackListening.APPLICATION_FOUND = i.toString();
                    TrackListening.APP_TRIGGER = true;
                }
                String[] str = c.split(",");
                String titleFound = str[0].substring(22);
                TrackListening.TRACK_FOUND = titleFound;
                TrackListening.IS_CHANGED = true;

                DatabaseHelper DbHelper = new DatabaseHelper(context);
                SQLiteDatabase db = DbHelper.getWritableDatabase();
                UserSettingsInterface userSettings = DbHelper.getUserSettings(db);

                ApplicationInfo i = (ApplicationInfo) b.get("android.appInfo");

                Log.e("i: ",i.toString());
                Log.e("FOUND: ", TrackListening.APPLICATION_FOUND);
                if (!TrackListening.TITLE_FOUND.equals(titleFound) && userSettings.getReceiveNotification() && TrackListening.APPLICATION_FOUND.equals(i.toString())){
                    Log.e("BROKEN",titleFound);
                    NotificationCreation.createNotification(getApplicationContext());
                    TrackListening.TITLE_FOUND = titleFound;
                }
            }
        }
    }
}
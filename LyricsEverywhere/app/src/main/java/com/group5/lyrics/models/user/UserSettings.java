package com.group5.lyrics.models.user;

public class UserSettings implements UserSettingsInterface {

    private final int id;
    private final boolean screenAlwaysActive;
    private final boolean favouriteLyricsOffline;
    private final boolean recentLyricsOffline;
    private boolean receiveNotification;
    private final int maxRecentToSave;

    //class constructor
    public UserSettings(int id, boolean screenAlwaysActive, boolean favouriteLyricsOffline, boolean recentLyricsOffline, boolean receiveNotification, int maxRecentToSave) {
        this.id = id;
        this.screenAlwaysActive = screenAlwaysActive;
        this.favouriteLyricsOffline = favouriteLyricsOffline;
        this.recentLyricsOffline = recentLyricsOffline;
        this.receiveNotification = receiveNotification;
        this.maxRecentToSave = maxRecentToSave;
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean getScreenAlwaysActive() {
        return screenAlwaysActive;
    }

    @Override
    public boolean getFavouriteLyricsOffline() {
        return favouriteLyricsOffline;
    }

    @Override
    public boolean getRecentLyricsOffline() {
        return recentLyricsOffline;
    }

    @Override
    public boolean getReceiveNotification() {
        return receiveNotification;
    }

    @Override
    public int getMaxRecentToSave() {
        return maxRecentToSave;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", screenAlwaysActive=" + screenAlwaysActive +
                ", favouriteLyricsOffline=" + favouriteLyricsOffline +
                ", recentLyricsOffline=" + recentLyricsOffline +
                ", receiveNotification=" + receiveNotification +
                ", maxRecentToSave=" + maxRecentToSave +
                '}';
    }
}
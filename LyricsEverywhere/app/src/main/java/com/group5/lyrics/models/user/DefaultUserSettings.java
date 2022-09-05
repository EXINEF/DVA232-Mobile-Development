package com.group5.lyrics.models.user;

public class DefaultUserSettings {

    static UserSettingsInterface setting = new UserSettings(
            0,
            true,
            true,
            true,
            true,
            20
    );

    public static UserSettingsInterface getTestSetting() {
        return setting;
    }
}



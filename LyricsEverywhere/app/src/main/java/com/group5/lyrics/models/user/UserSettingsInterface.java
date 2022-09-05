package com.group5.lyrics.models.user;

public interface UserSettingsInterface {

    /**
     * Return the userSetting id
     *
     * @return setting Id
     */
    int getId();

    /**
     * checks if the option screen always active is selected
     *
     * @return isSelected
     */
    boolean getScreenAlwaysActive();

    /**
     * checks if the option get favourite lyrics offline is selected
     *
     * @return isSelected
     */
    boolean getFavouriteLyricsOffline();

    /**
     * checks if the option get recent lyrics offline is selected
     *
     * @return isSelected
     */
    boolean getRecentLyricsOffline();

    /**
     * checks if the option of receiving notification is active
     *
     * @return receiveNotification
     */
    boolean getReceiveNotification();

    /**
     * gets the max number of recents to save and display in the homepage
     *
     * @return maxNumber
     */
    int getMaxRecentToSave();

}

package com.group5.lyrics.models.song;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.group5.lyrics.utilities.BoolIntConverter;

import java.util.Objects;

public class Track implements TrackInterface {

    private final int id;
    private final String name;
    private final ArtistInterface artist;
    private final AlbumInterface album;
    private LyricsInterface lyrics;
    private final boolean isExplicit;
    private final boolean hasLyrics;
    private boolean isFavourite;
    private boolean isRecent;
    private String creation_datetime;

    //class constructor
    public Track(int id, String name, ArtistInterface artist, AlbumInterface album, LyricsInterface lyrics, int isExplicit, int hasLyrics, int isFavourite, int isRecent) {
        this.id = id;
        this.name = name;
        this.hasLyrics = BoolIntConverter.getBoolFromInt(hasLyrics);
        this.isExplicit = BoolIntConverter.getBoolFromInt(isExplicit);
        this.lyrics = lyrics;
        this.album = album;
        this.artist = artist;
        this.isFavourite = BoolIntConverter.getBoolFromInt(isFavourite);
        this.isRecent = BoolIntConverter.getBoolFromInt(isRecent);
    }

    // default constructor
    public Track(int id, String name, ArtistInterface artist, AlbumInterface album, LyricsInterface lyrics, int isExplicit, int hasLyrics) {
        this.id = id;
        this.name = name;
        this.hasLyrics = BoolIntConverter.getBoolFromInt(hasLyrics);
        this.isExplicit = BoolIntConverter.getBoolFromInt(isExplicit);
        this.lyrics = lyrics;
        this.album = album;
        this.artist = artist;
        this.isFavourite = false;
        this.isRecent = true;
    }

    //class constructor
    public Track(int id, String name, ArtistInterface artist, AlbumInterface album, LyricsInterface lyrics, int isExplicit, int hasLyrics, int isFavourite, int isRecent, String creation_datetime) {
        this.id = id;
        this.name = name;
        this.hasLyrics = BoolIntConverter.getBoolFromInt(hasLyrics);
        this.isExplicit = BoolIntConverter.getBoolFromInt(isExplicit);
        this.lyrics = lyrics;
        this.album = album;
        this.artist = artist;
        this.isFavourite = BoolIntConverter.getBoolFromInt(isFavourite);
        this.isRecent = BoolIntConverter.getBoolFromInt(isRecent);
        this.creation_datetime = creation_datetime;
    }

    //all the methods that allow the access of the class object attributes
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasLyrics() {
        return hasLyrics;
    }

    @Override
    public boolean isExplicit() {
        return isExplicit;
    }

    @Override
    public boolean isFavourite() {
        return isFavourite;
    }

    @Override
    public boolean isRecent() {
        return isRecent;
    }

    @Override
    public void setFavourite(boolean value) {
        this.isFavourite = value;
    }

    @Override
    public void setRecent(boolean value) {
        this.isRecent = value;
    }

    @Override
    public LyricsInterface getLyrics() {
        return lyrics;
    }

    @Override
    public AlbumInterface getAlbum() {
        return album;
    }

    @Override
    public ArtistInterface getArtist() {
        return artist;
    }

    @Override
    public void setLyrics(LyricsInterface lyrics) {
        this.lyrics=lyrics;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n\nTrack{" +
                "\nid=" + id +
                ", \nname='" + name + '\'' +
                ", \nartist=" + artist +
                ", \nalbum=" + album +
                ", \nlyrics=" + lyrics +
                ", \nisExplicit=" + isExplicit +
                ", \nhasLyrics=" + hasLyrics +
                ", \nisFavourite=" + isFavourite +
                ", \nisRecent=" + isRecent +
                ", \ncreation_datetime=" + creation_datetime +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return id == track.id && isExplicit == track.isExplicit && hasLyrics == track.hasLyrics && isFavourite == track.isFavourite && isRecent == track.isRecent && Objects.equals(name, track.name) && Objects.equals(artist, track.artist) && Objects.equals(album, track.album) && Objects.equals(lyrics, track.lyrics) && Objects.equals(creation_datetime, track.creation_datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artist, album, lyrics, isExplicit, hasLyrics, isFavourite, isRecent, creation_datetime);
    }

}

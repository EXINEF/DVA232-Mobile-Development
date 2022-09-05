package com.group5.lyrics.models.song;

import java.util.Objects;

public class Artist implements ArtistInterface {

    private final int id;
    private final String name;

    public Artist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //methods to get values of the attributes of an object
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id && Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

package com.group5.lyrics.models.song;

import java.util.Objects;

public class Album implements AlbumInterface {

    private final int id;
    private final String name;

    public Album(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //methods to get attribute's values of the object
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
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return id == album.id && Objects.equals(name, album.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

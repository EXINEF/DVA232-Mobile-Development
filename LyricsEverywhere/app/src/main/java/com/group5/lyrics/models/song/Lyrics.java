package com.group5.lyrics.models.song;

import java.util.Objects;

public class Lyrics implements LyricsInterface {

    private final int id;
    private final String content;

    public Lyrics(int id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    //prints attribute of an instance
    public String toString() {
        return "Lyrics{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lyrics lyrics = (Lyrics) o;
        return id == lyrics.id && Objects.equals(content, lyrics.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }
}

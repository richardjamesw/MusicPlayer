package com.csc420.musicplayer;

public class Song {
    private String title, artist, album, genre;
    int resourceID;

    public Song (String title, String artist, String album, String genre, int resourceID)
    {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.resourceID = resourceID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getAlbum()
    {
        return album;
    }

    public String getGenre()
    {
        return genre;
    }

    public int getResourceID()
    {
        return resourceID;
    }

}

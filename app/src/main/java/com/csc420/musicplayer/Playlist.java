package com.csc420.musicplayer;

import java.util.ArrayList;

public class Playlist {
    String name;
    ArrayList<Song> songlist;

    public Playlist(String name)
    {
        this.name = name;
        songlist = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<Song> getSongs()
    {
        return songlist;
    }

    public int getNumSongs()
    {
        return songlist.size();
    }

    public void addSong(Song song)
    {
        songlist.add(song);
    }

    public void deleteSong(Song song)
    {
        songlist.remove(song);
    }
}

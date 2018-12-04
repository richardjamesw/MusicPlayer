package com.csc420.musicplayer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // private instances
    Button btnPlay;
    Button btnService;
    Button btnLyrics;
    Button btnSuggested;
    SeekBar positionBar;
    //SeekBar volumeBar;
    TextView lblElapsedTime;
    TextView lblRemainingTime;
    ListView lvMainWindowList;
    MediaPlayer player;
    int totalTime;
    List<Playlist> playlists;
    List<Song> songList;
    // This activity reference
    Context mainContext = this;
    ArrayAdapter<String> mainAdapter;
    // listeners
    android.widget.AdapterView.OnItemClickListener songListener;
    android.widget.AdapterView.OnItemClickListener mainListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load songs from disk
        LoadSongs();
        // Load Playlists from disk
        // LoadPlaylists();
        // Setup listeners
        InitListeners();

        lvMainWindowList = findViewById(R.id.lvMainWindowList);
        btnPlay = findViewById(R.id.btnPlay);
        lblElapsedTime = findViewById(R.id.lblElapsedTime);
        lblRemainingTime = findViewById(R.id.lblRemainingTime);

        // media player
        player = MediaPlayer.create(mainContext, R.raw.sample);
        player.setLooping(true);
        player.seekTo(0);
        player.setVolume(0.5f, 0.5f);
        totalTime = player.getDuration();

        btnSuggested = findViewById(R.id.btnSuggested);
        btnSuggested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSongs();
            }
        });

        // position bar
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser)
                        {
                            player.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

//        // volume bar
//        volumeBar = findViewById(R.id.volumeBar);
//        volumeBar.setOnSeekBarChangeListener(
//                new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        float volNum = progress / 100f;
//                        player.setVolume(volNum, volNum);
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                    }
//                }
//        );

        // Now start listening for main window list selections
        mainAdapter =
                new ArrayAdapter<>(mainContext,R.layout.main_window_list_layout, Constants.MainWindowList);
        SetMainWindowList();

        // Thread to update position bar and time label
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null)
                {
                    try
                    {
                        Message msg = new Message();
                        msg.what = player.getCurrentPosition();
                        handler.sendMessage(msg);

                        Thread.sleep(1000);
                    }
                    catch (InterruptedException exc)
                    {
                        System.out.println("ERROR: " + exc.getMessage());
                    }
                }
            }
        }).start();

        btnService = (Button) findViewById(R.id.btnService);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change back to main menu if we're not showing it
                if (lvMainWindowList.getAdapter() != mainAdapter)
                {
                    SetMainWindowList();
                }
                else
                {
                    // otherwise show the change service popup
                    startActivityForResult(new Intent(MainActivity.this, ServiceActivity.class), 1);
                }
            }
        });

        btnLyrics = findViewById(R.id.btnLyrics);
        btnLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LyricsActivity.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                int serviceID = data.getIntExtra("ServiceID", 0);
                //switch to pandora activity
                if(serviceID == 1){
                    startActivityForResult(new Intent(MainActivity.this, PandoraActivity.class), 1);
                }else {
                    btnService.setBackgroundResource(Constants.serviceLogos[serviceID]);
                }
            }
        }
    }

    private void SetMainWindowList() {
        // Set The Adapter and listener for main view
        if (mainAdapter != null
                && mainListener != null)
        {
            lvMainWindowList.setAdapter(mainAdapter);
            lvMainWindowList.setOnItemClickListener(mainListener);
        }
    }

    // handler for positioning
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            // update the time bar
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);
            // time labels
            String elapsedTime = createTimeLabel(currentPosition);
            lblElapsedTime.setText(elapsedTime);
            String remainingTime = createTimeLabel(totalTime - currentPosition);
            lblRemainingTime.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time)
    {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    // Play
    public void btnPlayClick(View view)
    {
        // check if we're playing currently
        if (!player.isPlaying())
        {
            // Play
            player.start();
            btnPlay.setBackgroundResource(R.drawable.pause);
        }
        else
        {
            // Stop
            player.pause();
            btnPlay.setBackgroundResource(R.drawable.play);
        }
    }

    // load songs from disk
    private void LoadSongs()
    {
        if (songList == null)
        {
            songList = new ArrayList<>();
        }
        // used to parse song file
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        try
        {
//            // Get all the song files and extract their data from raw resources
//            Field[] fields = R.raw.class.getFields();
//            // get out if theres no songs
//            if (fields == null || fields.length < 1) return;
//
//            // iterate through songs
//            for(int count=0; count < fields.length; count++){
//                int resourceID = fields[count].getInt(fields[count]);
//                Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/raw/" + resourceID);
//                mmr.setDataSource(mainContext, mediaPath);
//
//                // get song info from meta data
//                String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//                String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//                String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
//                String genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
//
//                // add to list of all songs
//                songList.add(new Song(songTitle, artist, album, genre, resourceID));
//            }

            // test data
            songList.add(new Song("Sicko Mode", "Travi$ Scott", "ASTROWORLD", "Rap", 001));
            songList.add(new Song("Mo Bamba", "Sheck Wes", "Mudboy", "Rap", 002));

        } catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // Get hashmap of local songs
    private HashMap<String, String> getSongMap() {
        if (songList == null)
            return null;

        HashMap<String, String> songs = new HashMap<>();
        // convert songs to map of title and artist
        for (Song s : songList)
        {
            songs.put(s.getTitle(), s.getArtist());
        }

        return songs;
    }

    // setup custom action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // when we click the search button
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Initialize listeners
    private void InitListeners()
    {
        // listener for songs menu
        songListener = new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                try
                {
                    HashMap<String, String> item = (HashMap<String, String>) adapter.getItemAtPosition(position);

                    // play song
                    System.out.println("test: ");
                }
                catch (Exception exc)
                {
                    System.out.println("Error: " + exc.getMessage());
                }

            }
        };

        // Listener for main window items
        mainListener = new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String item = (String) adapter.getItemAtPosition(position);

                // Check which item was selected and do something
                switch (item)
                {
                    case "Songs" :
                        loadSongs();
                        break;
                    case "Artists" :
                        break;
                    case "Albums" :
                        break;
                    case "Playlists" :
                        PlaylistsAdapter pa = new PlaylistsAdapter(MainActivity.this, Constants.playListNames);
                        lvMainWindowList.setAdapter(pa);
                        break;
                    case "Genres" :
                        ServiceAdapter sa = new ServiceAdapter(MainActivity.this, Constants.Genres, Constants.genreLogos);
                        lvMainWindowList.setAdapter(sa);
                        break;
                }
            }
        };
    }

    private void loadSongs(){
        // Get local songs
        // Use map for song name and artist name
        HashMap<String, String> songsMap = getSongMap();
        if (songsMap == null)
        {
            // let user know there are no songs available
            return;
        }
        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter listAdapter = new SimpleAdapter(mainContext, listItems, R.layout.song_list_layout,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.textSong, R.id.textArtist});


        Iterator it = songsMap.entrySet().iterator();
        while (it.hasNext())
        {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }
        // Update list view
        lvMainWindowList.setAdapter(listAdapter);
        // Change listener for songs
        lvMainWindowList.setOnItemClickListener(songListener);
    }
}

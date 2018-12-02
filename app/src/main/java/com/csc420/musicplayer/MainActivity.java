package com.csc420.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // private instances
    Button btnPlay;
    SeekBar positionBar;
    //SeekBar volumeBar;
    TextView lblElapsedTime;
    TextView lblRemainingTime;
    ListView lvMainWindowList;
    MediaPlayer player;
    int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMainWindowList = findViewById(R.id.lvMainWindowList);
        btnPlay = findViewById(R.id.btnPlay);
        lblElapsedTime = findViewById(R.id.lblElapsedTime);
        lblRemainingTime = findViewById(R.id.lblRemainingTime);

        // Main Window list (Songs, Artists, etc..)
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this,R.layout.main_window_list_layout, Constants.MainWindowList);
        // Set The Adapter
        lvMainWindowList.setAdapter(arrayAdapter);

        // media player
        player = MediaPlayer.create(this, R.raw.sample);
        player.setLooping(true);
        player.seekTo(0);
        player.setVolume(0.5f, 0.5f);
        totalTime = player.getDuration();

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
                    catch (InterruptedException exc) {}
                }
            }
        }).start();
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
}

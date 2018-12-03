package com.csc420.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
    Button btnService;
    Button btnLyrics;
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

        btnService = (Button) findViewById(R.id.btnService);
        btnService.setBackgroundResource(Constants.serviceLogos[0]);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ServiceActivity.class), 1);
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

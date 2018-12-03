package com.csc420.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PandoraActivity extends Activity {

    Button btnPlay;
    Button btnService;
    Button btnLyrics;
    SeekBar positionBar;
    TextView lblElapsedTime;
    TextView lblRemainingTime;
    ListView lvMainWindowList;
    MediaPlayer player;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pandora_activity);


        lvMainWindowList = findViewById(R.id.lvMainWindowList);
        btnPlay = findViewById(R.id.btnPlay);
        lblElapsedTime = findViewById(R.id.lblElapsedTime);
        lblRemainingTime = findViewById(R.id.lblRemainingTime);

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
        btnService.setBackgroundResource(Constants.serviceLogos[1]);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PandoraActivity.this, ServiceActivity.class), 1);
            }
        });

        btnLyrics = findViewById(R.id.btnLyrics);
        btnLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PandoraActivity.this, LyricsActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                int serviceID = data.getIntExtra("ServiceID", 0);
                if(serviceID != 1){
                    Intent intent = new Intent();
                    intent.putExtra("ServiceID", serviceID);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                //btnService.setBackgroundResource(Constants.serviceLogos[serviceID]);
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

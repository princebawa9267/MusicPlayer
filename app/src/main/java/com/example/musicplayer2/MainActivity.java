package com.example.musicplayer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView imageView,Previous,Play,Next;
    SeekBar songSeekBar,Volume;
    TextView MusicName;
    static MediaPlayer mediaPlayer;
    private Runnable runnable;
    private AudioManager audioManager;
    int currentIndex = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager)
                getSystemService(Context.AUDIO_SERVICE);
        Previous = findViewById(R.id.Previous);
        Play = findViewById(R.id.Play);
        Next = findViewById(R.id.Next);
        MusicName = findViewById(R.id.MusicName);
        imageView = findViewById(R.id.imageView);
        songSeekBar = findViewById(R.id.songSeekBar);
        Volume = findViewById(R.id.Volume);

        final ArrayList<Integer> songs = new ArrayList<>();
        songs.add(0,R.raw.doraemon);
        songs.add(1,R.raw.kiteretsu);
        songs.add(2,R.raw.ninja_hattori);
        songs.add(3,R.raw.pokemon);
        songs.add(4,R.raw.shinchan);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Volume.setMax(maxVolume);
        Volume.setProgress(currentVolume);
        Volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer != null&& mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    Play.setImageResource(R.drawable.baseline_play_arrow_24);
                }
                else
                {
                    mediaPlayer.start();
                    Play.setImageResource(R.drawable.baseline_pause_24);
                }
                songNames();
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null)
                {
                    Play.setImageResource(R.drawable.baseline_pause_24);
                }
                if(currentIndex<songs.size()-1)
                {
                    currentIndex++;
                }
                else {
                    currentIndex = 0 ;
                }
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
                mediaPlayer.start();
                songNames();
            }
        });
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null)
                {
                    Play.setImageResource(R.drawable.baseline_pause_24);
                }
                if(currentIndex>0)
                {
                    currentIndex--;
                }
                else
                {
                    currentIndex = songs.size()-1;
                }
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
                mediaPlayer.start();
                songNames();
            }
        });
    }
    private void songNames()
    {
        if(currentIndex == 0)
        {
            MusicName.setText("Doraemon - Title Song");
            imageView.setImageResource(R.drawable.doraemon_img);
        } else if (currentIndex == 1) {
            MusicName.setText("Kiteretsu - Title Song");
            imageView.setImageResource(R.drawable.kiteretsu_img);
        } else if (currentIndex == 2) {
            MusicName.setText("Ninja Hattori - Title Song");
            imageView.setImageResource(R.drawable.ninjahattori_img);
        } else if (currentIndex == 3) {
            MusicName.setText("Pokemon - Title Song");
            imageView.setImageResource(R.drawable.pokemon_img);
        } else if (currentIndex == 4) {
            MusicName.setText("Shinchen - Title Song");
            imageView.setImageResource(R.drawable.shinchen_img);
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                songSeekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
            }
        });
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                    songSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mediaPlayer != null)
                {
                    try {
                        if(mediaPlayer.isPlaying())
                        {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @SuppressLint("Handler Leak")  Handler handler = new Handler()
    {
        @Override
        public  void handleMessage(Message msg)
        {
            songSeekBar.setProgress(msg.what);
        }
    };
}
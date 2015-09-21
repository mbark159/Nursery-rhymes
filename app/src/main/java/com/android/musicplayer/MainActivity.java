package com.android.musicplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private Button b1,b2,b3,b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 25000;
    private int backwardTime = 25000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;

    public static int oneTimeOnly = 0;
    private static int currentSong = 1;
    static MediaPlayer.OnCompletionListener onCompletionListener;

    int music_numbers[] = { R.raw.song1, R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5 };
    int image_numbers[] = { R.drawable.rhyme1, R.drawable.rhyme1, R.drawable.rhyme2, R.drawable.rhyme3, R.drawable.rhyme4, R.drawable.rhyme5, R.drawable.rhyme6 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);

        tx1=(TextView)findViewById(R.id.textView2);
        tx2=(TextView)findViewById(R.id.textView3);
        tx3=(TextView)findViewById(R.id.textView4);
        tx3.setText("Song.mp3");

        iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.rhyme1);

        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        currentSong = 1;
        seekbar=(SeekBar)findViewById(R.id.seekBar);
        //seekbar.setClickable(false);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        b2.setEnabled(false);

        onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                if ((currentSong + 1) < 6) {
                    play_file(currentSong + 1);
                    playSong();
                }
            }
        };
        mediaPlayer.setOnCompletionListener(onCompletionListener);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if ((currentSong + 1) < 7) {
                    play_file(currentSong + 1);
                    playSong();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if ((currentSong - 1) > 0) {
                    play_file(currentSong - 1);
                    playSong();
                }
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",

                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private void playSong() {
        Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        // Improve this logic so that we dont need to set it everytime
        seekbar.setMax((int) finalTime);
        tx2.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );

        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
        b2.setEnabled(true);
        b3.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void play_file(int files) {
        currentSong = files;
        switch (files) {
            case 1:
                mediaPlayer = MediaPlayer.create(this, music_numbers[1]);
                iv.setImageResource(R.drawable.rhyme1);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, music_numbers[2]);
                iv.setImageResource(R.drawable.rhyme2);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, music_numbers[3]);
                iv.setImageResource(R.drawable.rhyme3);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, music_numbers[4]);
                iv.setImageResource(R.drawable.rhyme4);
                break;
            case 5:
                mediaPlayer = MediaPlayer.create(this, music_numbers[5]);
                iv.setImageResource(R.drawable.rhyme5);
                break;
            case 6:
                mediaPlayer = MediaPlayer.create(this, music_numbers[6]);
                iv.setImageResource(R.drawable.rhyme6);
                break;
        }
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }
}

package com.example.playertest;

import android.annotation.SuppressLint;

import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickInterface {
    //public static String FILE_PATH = "android.resource://com.example.playertest/raw/cyber";
    private MediaPlayer player;
    private AudioManager audioManager;
    private ArrayList<Track> tracksList;
    private ImageButton previousButton, nextButton;
    private MediaMetadataRetriever retriever;
    private ImageView albumArt;
    private FloatingActionButton play_pause_bt;
    private SeekBar mediaSeekbar;
    private TextView currentTime, endTime, trackTitle;
    private final Handler mHandler = new Handler();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int i = 0;
   // private Object object;
    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
        player.release();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_pause_bt = findViewById(R.id.play_pause_bt);
        tracksList = new ArrayList<>();
        tracksList.add(new Track(this,"android.resource://com.example.playertest/raw/cyber"));
        tracksList.add(new Track(this,"android.resource://com.example.playertest/raw/restor"));
        tracksList.add(new Track(this,"android.resource://com.example.playertest/raw/neon"));
        tracksList.add(new Track(this,"android.resource://com.example.playertest/raw/cibermidi"));
        tracksList.add(new Track(this,"android.resource://com.example.playertest/raw/lvs"));
        tracksList.add(new Track(this,"android.resource://com.example.playertest/raw/gyroscope"));


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(tracksList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);




        previousButton = findViewById(R.id.prev_bt);
        nextButton = findViewById(R.id.next_bt);

        initPlayer();
        play_pause_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!player.isPlaying()){
                    player.seekTo(mediaSeekbar.getProgress());
                    play_pause_bt.setImageResource(R.drawable.ic_pause);
                    player.start();
                    updSeekbar();

                } else {player.pause(); play_pause_bt.setImageResource(R.drawable.ic_play_arrow);}
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i<tracksList.size()-1) {
                    i++;
                } else {i=0;}
                player.seekTo(0);
                mediaSeekbar.setProgress(player.getCurrentPosition());
               if (player.isPlaying()) {
                   player.stop();
                   initPlayer();
                   play_pause_bt.setImageResource(R.drawable.ic_pause);
                   player.start();
               } else {initPlayer();}
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    i--;
                } else {i=tracksList.size()-1;}
                player.seekTo(0);
                mediaSeekbar.setProgress(player.getCurrentPosition());
                if (player.isPlaying()) {
                    player.stop();
                    initPlayer();
                    play_pause_bt.setImageResource(R.drawable.ic_pause);
                    player.start();
                } else {initPlayer();}

            }
        });

    }

    private void updSeekbar() {
        mediaSeekbar.setProgress(player.getCurrentPosition());
            currentTime.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(player.getCurrentPosition()), (TimeUnit.MILLISECONDS.toSeconds((long) player.getCurrentPosition()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                            toMinutes((long) player.getCurrentPosition())))));
            if(player.isPlaying()){
                Runnable upd = new Runnable() {
                    @Override
                    public void run() {
                        updSeekbar();
                    }
                };
                mHandler.postDelayed(upd, 100);
        }
    }

    @SuppressLint("DefaultLocale")
    private void initPlayer() {

            player = new MediaPlayer();
            retriever = new MediaMetadataRetriever();


        try {

            player.setDataSource(getApplicationContext(), Uri.parse(tracksList.get(i).getUrl()));
             retriever.setDataSource(getApplicationContext(), Uri.parse(tracksList.get(i).getUrl()));
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Can't load file", Toast.LENGTH_LONG).show();
        }


        if (player != null && retriever != null) {
           //todo String tracktitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
          //todo  String trackartist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.seekTo(0);
                    mediaSeekbar.setProgress(player.getCurrentPosition());
                    if (i<tracksList.size()-1) {
                        i++;
                    } else {i=0;}
                        player.stop();
                        initPlayer();
                        player.start();
                        play_pause_bt.setImageResource(R.drawable.ic_pause);

                }
            });

            mediaSeekbar = findViewById(R.id.mediaSeekbar);
            mediaSeekbar.setMax(player.getDuration());
            mediaSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser){
                        player.seekTo(progress);
                        currentTime.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(player.getCurrentPosition()), (TimeUnit.MILLISECONDS.toSeconds((long) player.getCurrentPosition()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) player.getCurrentPosition())))));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            endTime = findViewById(R.id.endtime_tv);
            currentTime = findViewById(R.id.curenttime_tv);
            trackTitle = findViewById(R.id.itemTracktitle_tv);
            albumArt = findViewById(R.id.albumcover_iv);
            albumArt.setImageBitmap(tracksList.get(i).getAlbumCover());
            trackTitle.setText(String.format("%s - %s", tracksList.get(i).getArtist(), tracksList.get(i).getTitle()));
            endTime.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(player.getDuration()), (TimeUnit.MILLISECONDS.toSeconds((long) player.getDuration()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                            toMinutes((long) player.getDuration())))));
            currentTime.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(player.getCurrentPosition()), (TimeUnit.MILLISECONDS.toSeconds((long) player.getCurrentPosition()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                            toMinutes((long) player.getCurrentPosition())))));

            if (!player.isPlaying()){play_pause_bt.setImageResource(R.drawable.ic_play_arrow);
           } else {player.pause(); play_pause_bt.setImageResource(R.drawable.ic_pause);}
        } else {Toast.makeText(getApplicationContext(), "You are not prepared!!!", Toast.LENGTH_LONG).show();}
    }


    @Override
    public void onItemClick(int position) {

        i = position;
        if (player.isPlaying()) {
            player.stop();
            initPlayer();
            play_pause_bt.setImageResource(R.drawable.ic_pause);
            player.start();} else {
        initPlayer();}
    }
}


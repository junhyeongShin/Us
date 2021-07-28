package com.example.us;
import android.widget.Toast;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import org.jetbrains.annotations.NotNull;

public class Activity_streaming_view extends AppCompatActivity implements Player.EventListener {
    PlayerView playerView_streaming;
    boolean fullscreen = false;
    String data;
    private static final String TAG = "Activity_streaming_view : ";
    SimpleExoPlayer player;
    boolean is_play ;
    long position ;

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        position = player.getCurrentPosition();

        System.out.println(TAG+"onConfigurationChanged");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_streaming_view_h);



        init();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_streaming_view);

        is_play = false;

    }

    @Override
    protected void onStop() {
        super.onStop();

        player.stop();
        player = null;
        finishAndRemoveTask();

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(TAG+"onStart");

        Intent intent_streaming = getIntent();
        data = intent_streaming.getStringExtra("hls_playback_url");
        System.out.println("streaming data : "+data);

        init();

    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println(TAG+"onResume");

    }

    private void init(){
        System.out.println(TAG+"init");

        playerView_streaming= findViewById(R.id.playerView_streaming);

        Uri uri = Uri.parse(data);
        player = new SimpleExoPlayer.Builder(this.getApplicationContext()).build();
        playerView_streaming.setPlayer(player);

        player.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if(isPlaying){
                    is_play = true;
                }else {
                    is_play = false;
                }
                    System.out.println(TAG+"is_play : "+is_play);

            }
        });

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Toast.makeText(getApplicationContext(), "종료된 방송 입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        // Build the media item.
        final MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();

        if(is_play){
            player.play();
            player.seekTo(position);
        }


    }

}
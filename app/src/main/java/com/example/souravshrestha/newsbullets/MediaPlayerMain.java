package com.example.souravshrestha.newsbullets;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Sourav Shrestha on 3/25/2018.
 */

public class MediaPlayerMain {

    public static String textLabel = "No Music To Play";
    public static MediaPlayer mp = new MediaPlayer();
    static int k = 0;
    public static void playIt(String x, Context con,Button playPause) throws IOException {
        if(!mp.isPlaying()){
            mp.start();
            playPause.setBackgroundResource(R.drawable.pause_button);
            k = 1;
        }else{
            mp.stop();
            mp.release();
            initializeMediaPlayer(x,con,playPause);
        }
    }

    public static void changeButton(Button x){
        if(k==0)
            x.setBackgroundResource(R.drawable.play_button);
        else
            x.setBackgroundResource(R.drawable.pause_button);
    }

    public static void pauseIt(Button playPause){
        if(k==1){
            mp.pause();
            playPause.setBackgroundResource(R.drawable.play_button);
            k = 0;
        }else{
            playPause.setBackgroundResource(R.drawable.pause_button);
            mp.start();
            k=1;
        }

    }

    public static void initializeMediaPlayer(final String x, final Context con,final Button playPause) {
        if(mp.isPlaying())
            mp.reset();
        mp = new MediaPlayer();
        try {
            mp.setDataSource(x);
            mp.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                try {
                    playIt(x,con,playPause);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}

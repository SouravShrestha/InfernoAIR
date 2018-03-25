package com.example.souravshrestha.newsbullets;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Sourav Shrestha on 3/25/2018.
 */

public class MediaPlayerMain {

    public static MediaPlayer mp = new MediaPlayer();

    public static void playIt(String x, Context con) throws IOException {
        if(!mp.isPlaying()){
            mp.start();
        }else{
            mp.stop();
            mp.release();
            initializeMediaPlayer(x,con);
        }
    }

    public static void initializeMediaPlayer(final String x,final Context con) {
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
                    playIt(x,con);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}

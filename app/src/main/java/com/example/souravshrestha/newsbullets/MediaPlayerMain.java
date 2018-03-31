package com.example.souravshrestha.newsbullets;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;

/**
 * Created by Sourav Shrestha on 3/25/2018.
 */

public class MediaPlayerMain {
    public static MediaPlayer mp = new MediaPlayer();
    static int k = 0;
    public static void playIt(String x, Context con) throws IOException {
        if(!mp.isPlaying()){
            mp.start();
            k = 1;
        }else{
            mp.stop();
            mp.release();
        }
    }

    public static void pauseIt(Button playPause){
        if(k==1){
            mp.pause();
            k = 0;
        }else{
            mp.start();
            k=1;
        }

    }

    public static void initializeMediaPlayer(final String x, final Context con) {
        FancyToast.makeText(con,"Track Is Loading. Please Wait!",FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();
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
                    FancyToast.makeText(con,"Now Playing",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}

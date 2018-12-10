package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class SoundEffects{
    // class for playing sound effects
    private Context context;
    private ArrayList<MediaPlayer> medialist;

    public SoundEffects(Context context){
        medialist = new ArrayList<MediaPlayer>();
        this.context = context;

        //ADD SOUND HERE
        MediaPlayer mp = MediaPlayer.create(context, R.raw.lasersound);
        medialist.add(mp);
        mp = MediaPlayer.create(context, R.raw.explosion);
        medialist.add(mp);
        mp = MediaPlayer.create(context, R.raw.mediumexplosion);
        medialist.add(mp);
        mp = MediaPlayer.create(context, R.raw.largeexplosion);
        medialist.add(mp);
        mp = MediaPlayer.create(context, R.raw.asteroiddamage);
        medialist.add(mp);
        mp = MediaPlayer.create(context, R.raw.waveincoming);
        medialist.add(mp);
        mp = MediaPlayer.create(context, R.raw.spaceshiphit);
        medialist.add(mp);
    }

    public void playLaser(){
        medialist.get(0).start();
    }

    public void playExplosion(){
        medialist.get(1).start();
    }

    public void playMedExplosion(){
        medialist.get(2).start();
    }

    public void playLargeExplosion(){
        medialist.get(3).start();
    }

    public void playAsteroidHit(){
        medialist.get(4).start();
    }

    public void playWaveIncoming(){
        medialist.get(5).start();
    }

    public void playShipHit(){
        medialist.get(6).start();
    }
}

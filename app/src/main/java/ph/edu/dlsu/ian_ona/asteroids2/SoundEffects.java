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
        //LASER SOUND
        MediaPlayer mp = MediaPlayer.create(context, R.raw.lasersound);
        medialist.add(mp);
        mp = MediaPlayer.create(context, R.raw.explosion);
        medialist.add(mp);
        //REST OF THE SOUNDS HERE
    }

    public void playLaser(){
        medialist.get(0).start();
    }

    public void playExplosion(){
        medialist.get(1).start();
    }
}

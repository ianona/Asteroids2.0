package ph.edu.dlsu.ian_ona.asteroids2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class BGMusicService extends Service {

    private MediaPlayer player;
    private boolean pause = false;
    private boolean stop = false;
    private boolean initial = true;
    private int pauseTime;

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.bitsong);
        player.setVolume(50,50);
        player.setLooping(true);
        Log.d("Service MusicService","onCreate Initialize");
    }

    public void updatePlayer(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPref.getString(getString(R.string.pref_username), null);
        if (username != null && username.equalsIgnoreCase(getString(R.string.easterEgg1)))
            player = MediaPlayer.create(this, R.raw.yes);
        else if (username != null && username.equalsIgnoreCase(getString(R.string.easterEgg2)))
            player = MediaPlayer.create(this, R.raw.stars);
        else if (username != null && username.equalsIgnoreCase(getString(R.string.easterEgg3)))
            player = MediaPlayer.create(this, R.raw.yes);
        else player = MediaPlayer.create(this, R.raw.bitsong);
    }

    public void toggleMusic(boolean play){
        player.setVolume(0, 0);
        if (play)
            player.setVolume(50, 50);
    }

    public void playMusic () {
        if (initial){
            player.start();
            initial = false;
        }

        if(stop){
            player.start();
            Log.d("Service MusicService","MUSIC PLAYED");
            stop = false;
        }
        else if (pause){
            player.seekTo(pauseTime);
            player.start();
            Log.d("Service MusicService","MUSIC RESUMED");
            pause = false;
        }
    }

    public void stopMusic () {
        stop = true;
        pause = false;
        player.stop();
        Log.d("Service MusicService","MUSIC STOPPED");

    }

    public void pauseMusic () {
        pause = true;
        stop = false;
        pauseTime = player.getCurrentPosition();
        player.pause();
        Log.d("Service MusicService","MUSIC PAUSED");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //player.start();
        Log.d("Service MusicService","onStartCommand Play");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        Log.d("Service MusicService","onDestroy Stop");
    }

    //(x) Binding related code
    private final IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        BGMusicService getService() {
            return BGMusicService.this;
        }
    }
}

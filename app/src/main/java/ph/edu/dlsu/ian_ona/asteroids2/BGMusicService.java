package ph.edu.dlsu.ian_ona.asteroids2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BGMusicService extends Service {

    private MediaPlayer player;
    private boolean pause = false;
    private boolean stop = false;
    private boolean initial = true;

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.bitsong);
        player.setVolume(50,50);
        player.setLooping(true);
        Log.d("Service MusicService","onCreate Initialize");
    }

    public void playMusic () {
        if (initial){
            player.start();
            initial = false;
        }

        if(stop){
            player.seekTo(0);
            player.start();
            Log.d("Service MusicService","MUSIC PLAYED");
            stop = false;
        }
        else if (pause){
            player.start();
            Log.d("Service MusicService","MUSIC PLAYED");
            pause = false;
        }
    }

    public void stopMusic () {
        stop = true;
        player.pause();
        Log.d("Service MusicService","MUSIC STOPPED");

    }

    public void pauseMusic () {
        pause = true;
        player.pause();
        Log.d("Service MusicService","MUSIC STOPPED");

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

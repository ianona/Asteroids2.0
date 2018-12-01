package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean bound;
    private BGMusicService musicService;
    private Intent musicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.PIXEL_FONT = Typeface.createFromAsset(getAssets(),  "fonts/thinpixel.ttf");
        setContentView(R.layout.activity_main);

        musicIntent = new Intent(this, BGMusicService.class);
        startService(musicIntent);
        if(!bound)
            bindService(musicIntent, mConnection, BIND_AUTO_CREATE);
    }

    public void startGame(View view){
        musicService.playMusic();

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        MainActivity.this.startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (bound)
            musicService.stopMusic();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(bound)
            unbindService(mConnection);
        stopService(musicIntent);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            BGMusicService.LocalBinder binder = (BGMusicService.LocalBinder) service;
            musicService = (binder.getService());
            bound = true;

            Log.d(" - SERVICE","ServiceConnection made");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}

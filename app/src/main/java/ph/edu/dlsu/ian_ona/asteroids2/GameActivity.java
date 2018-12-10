package ph.edu.dlsu.ian_ona.asteroids2;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
    private GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gamePanel = new GamePanel(this);
        setContentView(gamePanel);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        gamePanel.pause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        MainActivity.musicService.stopMusic();
    }
}

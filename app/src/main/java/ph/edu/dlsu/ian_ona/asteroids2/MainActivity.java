package ph.edu.dlsu.ian_ona.asteroids2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private boolean bound;
    public static BGMusicService musicService;
    private Intent musicIntent;

    private EditText usernameTxt;
    private TextView highscoreTxt, titleText, welcomeTxt;
    private ImageButton soundBtn;
    private String[] highscores;
    private int[] highscoreList;

    private final String TAG = Constants.getTAG(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.MAIN_CONTEXT = MainActivity.this;
        Constants.PIXEL_FONT = Typeface.createFromAsset(getAssets(),  "fonts/thinpixel.ttf");
        Constants.HIGHSCORES = 5;
        setContentView(R.layout.activity_main);

        // NOTE: binding is async
        musicIntent = new Intent(this, BGMusicService.class);
        startService(musicIntent);
        if(!bound)
            bindService(musicIntent, mConnection, BIND_AUTO_CREATE);

        titleText = (TextView)findViewById(R.id.titleText);
        titleText.setTypeface(Constants.PIXEL_FONT);
        welcomeTxt = findViewById(R.id.welcome);
        welcomeTxt.setTypeface(Constants.PIXEL_FONT);
        usernameTxt = findViewById(R.id.usernameTxt);
        usernameTxt.setTypeface(Constants.PIXEL_FONT);
        highscores = new String[Constants.HIGHSCORES];
        highscoreList = new int[Constants.HIGHSCORES];
        highscoreTxt = findViewById(R.id.highscoreTxt);
        highscoreTxt.setTypeface(Constants.PIXEL_FONT);
        soundBtn = findViewById(R.id.soundButton);

        initializeMenu();
        updateScoreView();
    }

    public void didTapButton(View view) {
        ImageButton playButton = findViewById(R.id.playButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 5);
        myAnim.setInterpolator(interpolator);

        playButton.startAnimation(myAnim);

        startGame(view);
    }

    public void startGame(View view){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_username), usernameTxt.getText().toString());
        editor.commit();

        if (sharedPref.getString(getString(R.string.pref_username), null).isEmpty()) {
            Toast.makeText(this, "PLEASE ENTER A USERNAME", Toast.LENGTH_LONG).show();
        } else {
            musicService.updatePlayer();
            musicService.playMusic();
            if ((Integer)soundBtn.getTag() == R.drawable.sound) {
                musicService.toggleMusic(true);
            } else {
                musicService.toggleMusic(false);
            }
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

    public void didTapButton1(View view) {
        ImageButton settingButton = findViewById(R.id.settingButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 5);
        myAnim.setInterpolator(interpolator);

        settingButton.startAnimation(myAnim);

        gotoSettings(view);
    }

    public void gotoSettings(View view){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_username), usernameTxt.getText().toString());
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void didTapButton2(View view) {
        ImageButton soundButton = findViewById(R.id.soundButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 5);
        myAnim.setInterpolator(interpolator);

        soundButton.startAnimation(myAnim);

        toggleSound(view);
    }

    public void toggleSound(View view){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        if ((Integer)soundBtn.getTag() == R.drawable.sound) {
            soundBtn.setImageResource(R.drawable.nosound);
            soundBtn.setTag(R.drawable.nosound);
            editor.putString(getString(R.string.pref_sound), getString(R.string.mute));
            Constants.MUTE = true;
        } else {
            soundBtn.setImageResource(R.drawable.sound);
            soundBtn.setTag(R.drawable.sound);
            editor.putString(getString(R.string.pref_sound), getString(R.string.sound));
            Constants.MUTE = false;
        }
        editor.commit();
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_username), usernameTxt.getText().toString());
        editor.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateScoreView();
        if (bound) {
            musicService.stopMusic();
        }
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

    public boolean updateHighScoreList(int score){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPref.getString(getString(R.string.pref_username), null);
        // Loops through highs cores and checks if there is empty slot OR if score is high enough
        // returns true if new score can be inserted
        // returns false otherwise
        for(int i = 0; i < Constants.HIGHSCORES; i++){
            if (highscoreList[i] == -1) {
                // if empty slot (-1) put score there
                highscores[i] = username + "@&&@" + score;
                highscoreList[i] = score;

                // update score preferences
                updateScorePreferences();
                return true;
            }
            else if(score >= highscoreList[i]){
                // if score is >= score in list, adjust list accordingly
                adjustList(score, i, username);

                // update score preferences
                updateScorePreferences();
                return true;
            }
        }
        return false;
    }

    public void adjustList(int score, int position, String username){
        // adjusts list at position to make room for score belonging to username
        for (int i = (Constants.HIGHSCORES-1); i >= 0; i--) {
            if (i == position) {
                highscoreList[position] = score;
                highscores[position] = username + "@&&@" + score;
                break;
            } else {
                int newScore = highscoreList[i - 1];
                highscoreList[i] = newScore;
                if (newScore == -1) highscores[i] = "-";
                else highscores[i] = highscores[i - 1].split("@&&@")[0] + "@&&@" + newScore;
            }
        }
    }

    public void initializeMenu(){
        // initializes username TextView based on current preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameTxt.setText(sharedPref.getString(getString(R.string.pref_username), null));

        // initialize sound
        String soundPref = sharedPref.getString(getString(R.string.pref_sound), getString(R.string.sound));
        if (soundPref.equalsIgnoreCase(getString(R.string.sound))){
            soundBtn.setImageResource(R.drawable.sound);
            soundBtn.setTag(R.drawable.sound);
            Constants.MUTE = false;
        } else {
            soundBtn.setImageResource(R.drawable.nosound);
            soundBtn.setTag(R.drawable.nosound);
            Constants.MUTE = true;
        }

        // initializes in-game arrays based on current preferences
        for (int i=0;i<Constants.HIGHSCORES;i++){
            // name and score will be separated by: @&&@
            highscores[i] = sharedPref.getString(getString(getResources().getIdentifier("pref_highscore"+(i+1), "string", getPackageName())), "-");
            if (highscores[i].equalsIgnoreCase("-"))
                highscoreList[i] = -1;
            else highscoreList[i] = Integer.parseInt(highscores[i].split("@&&@")[1]);
        }
    }

    public void updateScorePreferences(){
        // updates preferences with in-game array values
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i=0;i<Constants.HIGHSCORES;i++){
            editor.putString(getString(getResources().getIdentifier("pref_highscore"+(i+1), "string", getPackageName())), highscores[i]);
            editor.commit();
        }
    }

    public void updateScoreView(){
        // updates view elements with proper scores based on initialized arrays
        highscoreTxt.setText("HIGH SCORES");
        for (int i=0;i<Constants.HIGHSCORES;i++){
            if (!highscores[i].equalsIgnoreCase("-"))
                highscoreTxt.setText(highscoreTxt.getText().toString() + "\n" + highscores[i].split("@&&@")[0] + " ... " + highscores[i].split("@&&@")[1]);
            else highscoreTxt.setText(highscoreTxt.getText().toString() + "\n" + highscores[i]);
        }
    }
}

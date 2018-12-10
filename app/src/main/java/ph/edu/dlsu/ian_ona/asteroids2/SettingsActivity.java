package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private ImageButton mapOption1;
    private ImageButton mapOption2;
    private ImageButton mapOption3;

    private ImageButton spaceOption1;
    private ImageButton spaceOption2;
    private ImageButton spaceOption3;

    private ImageButton tiltOption1;
    private ImageButton tiltOption2;
    private ImageButton tiltOption3;

    private Button saveBtn;
    private Button cancelBtn;

    private TextView tiltTxt;
    private TextView spaceTxt;
    private TextView mapTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        saveBtn = findViewById(R.id.settings_saveBtn);
        cancelBtn = findViewById(R.id.settings_cancelBtn);
        saveBtn.setTypeface(Constants.PIXEL_FONT);
        cancelBtn.setTypeface(Constants.PIXEL_FONT);

        mapTxt = findViewById(R.id.mapText);
        spaceTxt = findViewById(R.id.spaceshipText);
        tiltTxt = findViewById(R.id.tiltText);
        mapTxt.setTypeface(Constants.PIXEL_FONT);
        spaceTxt.setTypeface(Constants.PIXEL_FONT);
        tiltTxt.setTypeface(Constants.PIXEL_FONT);

        mapOption1 = findViewById(R.id.mapOption1);
        mapOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapOption1.setBackgroundResource(R.drawable.customborder);
                mapOption2.setBackgroundResource(0);
                mapOption3.setBackgroundResource(0);
            }
        });
        mapOption2 = findViewById(R.id.mapOption2);
        mapOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapOption2.setBackgroundResource(R.drawable.customborder);
                mapOption1.setBackgroundResource(0);
                mapOption3.setBackgroundResource(0);
            }
        });
        mapOption3 = findViewById(R.id.mapOption3);

        mapOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapOption3.setBackgroundResource(R.drawable.customborder);
                mapOption2.setBackgroundResource(0);
                mapOption1.setBackgroundResource(0);
            }
        });

        spaceOption1 = findViewById(R.id.spaceOption1);
        spaceOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spaceOption1.setBackgroundResource(R.drawable.customborder);
                spaceOption2.setBackgroundResource(0);
                spaceOption3.setBackgroundResource(0);
            }
        });
        spaceOption2 = findViewById(R.id.spaceOption2);
        spaceOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spaceOption2.setBackgroundResource(R.drawable.customborder);
                spaceOption1.setBackgroundResource(0);
                spaceOption3.setBackgroundResource(0);
            }
        });
        spaceOption3 = findViewById(R.id.spaceOption3);
        spaceOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spaceOption3.setBackgroundResource(R.drawable.customborder);
                spaceOption2.setBackgroundResource(0);
                spaceOption1.setBackgroundResource(0);
            }
        });


        tiltOption1 = findViewById(R.id.tiltOption1);
        tiltOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiltOption1.setBackgroundResource(R.drawable.customborder);
                tiltOption2.setBackgroundResource(0);
                tiltOption3.setBackgroundResource(0);
            }
        });
        tiltOption2 = findViewById(R.id.tiltOption2);
        tiltOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiltOption2.setBackgroundResource(R.drawable.customborder);
                tiltOption1.setBackgroundResource(0);
                tiltOption3.setBackgroundResource(0);
            }
        });
        tiltOption3 = findViewById(R.id.tiltOption3);
        tiltOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiltOption3.setBackgroundResource(R.drawable.customborder);
                tiltOption2.setBackgroundResource(0);
                tiltOption1.setBackgroundResource(0);
            }
        });

        mapOption1.setBackgroundResource(R.drawable.customborder);
        mapOption2.setBackgroundResource(R.drawable.customborder);
        mapOption3.setBackgroundResource(R.drawable.customborder);
        spaceOption1.setBackgroundResource(R.drawable.customborder);
        spaceOption2.setBackgroundResource(R.drawable.customborder);
        spaceOption3.setBackgroundResource(R.drawable.customborder);
        tiltOption1.setBackgroundResource(R.drawable.customborder);
        tiltOption2.setBackgroundResource(R.drawable.customborder);
        tiltOption3.setBackgroundResource(R.drawable.customborder);

        mapOption1.setBackgroundResource(0);
        mapOption2.setBackgroundResource(0);
        mapOption3.setBackgroundResource(0);
        spaceOption1.setBackgroundResource(0);
        spaceOption2.setBackgroundResource(0);
        spaceOption3.setBackgroundResource(0);
        tiltOption1.setBackgroundResource(0);
        tiltOption2.setBackgroundResource(0);
        tiltOption3.setBackgroundResource(0);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        switch (sharedPref.getInt(getString(R.string.pref_background), R.string.map1)){
            case R.string.map1:
                mapOption1.setBackgroundResource(R.drawable.customborder);
                break;
            case R.string.map2:
                mapOption2.setBackgroundResource(R.drawable.customborder);
                break;
            case R.string.map3:
                mapOption3.setBackgroundResource(R.drawable.customborder);
                break;
        }
        switch (sharedPref.getInt(getString(R.string.pref_spaceship), R.string.space1)){
            case R.string.space1:
                spaceOption1.setBackgroundResource(R.drawable.customborder);
                break;
            case R.string.space2:
                spaceOption2.setBackgroundResource(R.drawable.customborder);
                break;
            case R.string.space3:
                spaceOption3.setBackgroundResource(R.drawable.customborder);
                break;
        }
        switch (sharedPref.getInt(getString(R.string.pref_motion), R.string.tilt1)){
            case R.string.tilt1:
                tiltOption1.setBackgroundResource(R.drawable.customborder);
                break;
            case R.string.tilt2:
                tiltOption2.setBackgroundResource(R.drawable.customborder);
                break;
            case R.string.tilt3:
                tiltOption3.setBackgroundResource(R.drawable.customborder);
                break;
        }
    }

    public void cancel (View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        SettingsActivity.this.startActivity(intent);
    }

    public void save (View view){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (mapOption1.getBackground() != null)
            editor.putInt(getString(R.string.pref_background), R.string.map1);
        else if (mapOption2.getBackground() != null)
            editor.putInt(getString(R.string.pref_background), R.string.map2);
        else if (mapOption3.getBackground() != null)
            editor.putInt(getString(R.string.pref_background), R.string.map3);

        if (spaceOption1.getBackground() != null)
            editor.putInt(getString(R.string.pref_spaceship), R.string.space1);
        else if (spaceOption2.getBackground() != null)
            editor.putInt(getString(R.string.pref_spaceship), R.string.space2);
        else if (spaceOption3.getBackground() != null)
            editor.putInt(getString(R.string.pref_spaceship), R.string.space3);

        if (tiltOption1.getBackground() != null)
            editor.putInt(getString(R.string.pref_motion), R.string.tilt1);
        else if (tiltOption2.getBackground() != null)
            editor.putInt(getString(R.string.pref_motion), R.string.tilt2);
        else if (tiltOption3.getBackground() != null)
            editor.putInt(getString(R.string.pref_motion), R.string.tilt3);

        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SettingsActivity.this.startActivity(intent);
    }
}

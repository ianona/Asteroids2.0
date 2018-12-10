package com.example.wonsukcho.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setting font
        TextView tx = (TextView)findViewById(R.id.welcome);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/thinpixel.ttf");
        tx.setTypeface(custom_font);
        EditText et = (EditText)findViewById(R.id.userEdit);
        et.setTypeface(custom_font);
        TextView titleText = (TextView)findViewById(R.id.titleText);
        titleText.setTypeface(custom_font);
        TextView highscoreList = (TextView)findViewById(R.id.highscoreList);
        highscoreList.setTypeface(custom_font);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}

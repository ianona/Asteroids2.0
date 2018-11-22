package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AsteroidManager {
    private ArrayList<Asteroid> asteroids;
    private long startTime, initTime;
    private Bitmap bmp;

    private final String TAG = Constants.getTAG(this);
    private Random random = new Random();
    private int finalScore;
    private boolean gameOver;

    // for asteroid spawning, a little sphagetti
    int prev = -1;
    int interval;

    public AsteroidManager (Bitmap bmp) {
        asteroids = new ArrayList<>();
        this.bmp = bmp;
        startTime = initTime = System.currentTimeMillis();
        gameOver = false;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public boolean shipCollide (SpaceShip player) {
        for (Asteroid a:asteroids) {
            if (!a.isDestroyed() && !a.isExploded() && a.shipCollide(player) && player.isBuffered()) {
                player.takeDmg(15);
                a.explode();
                a.setExploded(true);
                if (player.getHealth() <= 0) {
                    player.setDmgBuffer(0);
                    finalScore = (int)((System.currentTimeMillis()-initTime)/1000.0);
                    return true;
                }
            }
        }
        return false;
    }

    public void ammoCollide (Ammo ammo) {
        for (Asteroid a:asteroids) {
            if (!a.isDestroyed() && !a.isExploded() && a.ammoCollide(ammo)) {
                a.explode();
                ammo.setHit(true);
                a.setExploded(true);
            }
        }
    }

    public void update(){
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        // tinker with this to adjust speed as time passes
        // 10000: speed up every 10 seconds
        // Constants.SCREEN_HEIGHT/10000: take 10 seconds to traverse the screen
        float speed = (float)(Math.sqrt((startTime - initTime)/10000.0)) * Constants.SCREEN_HEIGHT/10000.0f;
        for (Asteroid a : asteroids) {
            if (!a.isExploded())
                a.incrementY(speed * elapsedTime);
            else a.updateExplosion();
        }

        // generate asteroids at 2 second intervals
        interval = (int)((System.currentTimeMillis()-initTime)/1000.0);
        if (interval % 2 == 0 && interval!=prev) {
            generateAsteroids(random.nextInt(3)+3);
            prev = interval;
        }
    }

    public void generateAsteroids(int aNum){
        for (int i = 0; i < aNum; i++) {
            int height = bmp.getHeight()/4;
            int width = bmp.getWidth()/6;
            int curY = (-height * 3) + random.nextInt(height * 2);
            int curX = random.nextInt(Constants.SCREEN_WIDTH);
            asteroids.add(0, new Asteroid(new Rect(curX,curY,curX+width,curY+height), bmp, 4));
        }
    }

    public void draw(Canvas canvas){
        for (Asteroid a : asteroids) {
            a.draw(canvas);
        }
        Paint p = new Paint();
        p.setTextSize(200);
        p.setColor(Color.WHITE);
        p.setTypeface(Constants.PIXEL_FONT);
        if (!gameOver)
            canvas.drawText(""+(int)((System.currentTimeMillis()-initTime)/1000.0), 50,  100, p);
    }

    public int getScore(){
        gameOver = true;
        return finalScore;
    }
}

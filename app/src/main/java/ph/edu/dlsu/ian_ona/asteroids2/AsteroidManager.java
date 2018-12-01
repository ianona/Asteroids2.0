package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class AsteroidManager {
    private ArrayList<Asteroid> asteroids;
    private long startTime, initTime;
    private Bitmap bmp;
    private SoundEffects se;

    private final String TAG = Constants.getTAG(this);
    private Random random = new Random();
    private int finalScore;
    private boolean gameOver;

    // for asteroid spawning, a little sphagetti
    int prev = -1;
    int interval;

    private int minAsteroids;

    public AsteroidManager (Bitmap bmp) {
        asteroids = new ArrayList<>();
        this.bmp = bmp;
        startTime = initTime = System.currentTimeMillis();
        gameOver = false;
        minAsteroids = 3;
        se = new SoundEffects(Constants.CURRENT_CONTEXT);
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public boolean shipCollide (SpaceShip player) {
        for (Asteroid a:asteroids) {
            if (!a.isDestroyed() && !a.isExploded() && a.shipCollide(player) && player.isBuffered()) {
                player.takeDmg(a.getDamage());
                a.explode();
                a.setExploded(true);
                se.playExplosion();
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
                se.playExplosion();
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
                a.incrementY();
                //a.incrementY(speed * elapsedTime);
            else a.updateExplosion();
        }

        // generate asteroids at 2 second intervals
        interval = (int)((System.currentTimeMillis()-initTime)/1000.0);

        if (interval % 2 == 0 && interval!=prev) {
            // every 60 seconds, increase min. # of asteroids
            if (interval % 60 == 0)
                minAsteroids++;
            generateAsteroids(random.nextInt(minAsteroids)+(minAsteroids));
            prev = interval;
        }
    }

    public void generateAsteroids(int aNum){
        for (int i = 0; i < aNum; i++) {
            int max_height = bmp.getHeight();
            int curY = (-max_height * 3) + random.nextInt(max_height * 2);
            int curX = random.nextInt(Constants.SCREEN_WIDTH);

            // 30% chance to generate medium asteroid
            if (random.nextDouble() < 0.30)
                asteroids.add(0, new MediumAsteroid(curX, curY, bmp, 2));
            // 10% chance to generate large asteroid
            else if (random.nextDouble() < 0.10)
                asteroids.add(0, new LargeAsteroid(curX, curY, bmp, 1));
            else // generate small asteroid
                asteroids.add(0, new SmallAsteroid(curX, curY, bmp, 4));
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

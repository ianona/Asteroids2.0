package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class AsteroidManager {
    private ArrayList<Asteroid> asteroids;
    private int asteroidGap;
    private long startTime, initTime;
    private Bitmap bmp;

    private final String TAG = Constants.getTAG(this);
    private Random random = new Random();

    public AsteroidManager (int asteroidGap, Bitmap bmp) {
        asteroids = new ArrayList<>();
        this.bmp = bmp;
        this.asteroidGap = asteroidGap;
        startTime = initTime = System.currentTimeMillis();
        generateAsteroids();
    }

    public void generateAsteroids(){
        int curY = -5*Constants.SCREEN_HEIGHT/4;
        int height = bmp.getHeight()/4;
        int width = bmp.getWidth()/6;

        // keep generating asteroids while the last one hasn't entered the screen the screen
        while (curY < 0) {
            asteroids.add(new Asteroid(new Rect(300,curY,300+width,curY+height), bmp, 4));
            curY += height + asteroidGap;
        }
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public boolean shipCollide (SpaceShip player) {
        for (Asteroid a:asteroids) {
            if (!a.isDestroyed() && a.shipCollide(player))
                return true;
        }
        return false;
    }

    public void ammoCollide (Ammo ammo) {
        for (Asteroid a:asteroids) {
            if (!a.isDestroyed() && a.ammoCollide(ammo)) {
                a.setDestroyed(true);
                ammo.setHit(true);
            }
        }
    }

    public void update(){
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        // tinker with this to adjust speed as time passes
        float speed = (float)(Math.sqrt((startTime - initTime)/10000.0)) * Constants.SCREEN_HEIGHT/10000.0f;

        for (Asteroid a : asteroids) {
            a.incrementY(speed * elapsedTime);
        }

        // if last asteroid is off screen, add new asteroid
        if (asteroids.get(asteroids.size()-1).getPos().top >= Constants.SCREEN_HEIGHT) {
            int height = bmp.getHeight()/4;
            int width = bmp.getWidth()/6;
            int curY = asteroids.get(0).getPos().top - bmp.getHeight()/4 - asteroidGap;
            asteroids.add(0, new Asteroid(new Rect(500,curY,500+width,curY+height), bmp, 4));
            asteroids.remove(asteroids.size()-1);
        }
    }

    public void draw(Canvas canvas){
        for (Asteroid a : asteroids) {
            a.draw(canvas);
        }
    }
}

package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

public class AmmoManager {
    private ArrayList<Ammo> shots;
    private long startTime, initTime;
    private Bitmap bmp;

    private final String TAG = Constants.getTAG(this);

    public AmmoManager (Bitmap bmp) {
        shots = new ArrayList<>();
        this.bmp = bmp;
        startTime = initTime = System.currentTimeMillis();
    }

    public ArrayList<Ammo> getShots() {
        return shots;
    }

    public void update(){
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        // tinker with this to adjust speed as time passes
        float speed = (float)(Math.sqrt((startTime - initTime)/10000.0)) * Constants.SCREEN_HEIGHT/4000.0f;

        for (Ammo a : shots) {
            if (!a.isHit())
                a.decrementY(speed * elapsedTime * 2);
        }

        // lol try to remove ammo that's offscreen
        if (!shots.isEmpty() && shots.get(shots.size()-1).getPos().top >= Constants.SCREEN_HEIGHT) {
            shots.remove(shots.size()-1);
        }
    }

    public void draw(Canvas canvas){
        for (Ammo a : shots) {
            if (!a.isHit())
                a.draw(canvas);
        }
    }

    public void shoot(int x, int y){
        Point p = new Point();
        p.set(x,y);
        shots.add(0, new Ammo(p,bmp));
    }
}

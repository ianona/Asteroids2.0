package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class LargeAsteroid extends Asteroid{
    protected LargeAsteroid(int x, int y, Bitmap bmp, int asteroidType) {
        super(bmp, asteroidType);

        health = 15 * 10;
        damage = 60;
        // for medium asteroids
        width = bmp.getWidth()/2;
        height = bmp.getHeight()/1;

        srcX = 0;
        srcY = rand.nextInt(asteroidType) * height;

        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        pos = new Rect(x, y, x+width, y+height);
    }
}

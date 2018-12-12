package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class MediumAsteroid extends Asteroid{
    protected MediumAsteroid(int x, int y, Bitmap bmp, int asteroidType) {
        super(bmp, asteroidType);

        reload = 5;
        health = 15 * reload;
        damage = 40;

        // for medium asteroids
        width = bmp.getWidth()/3;
        height = bmp.getHeight()/2;

        srcX = bmp.getWidth()/2;
        srcY = rand.nextInt(asteroidType) * height;

        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        pos = new Rect(x, y, x+width, y+height);
    }
}

package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class SmallAsteroid extends Asteroid {
    public SmallAsteroid(int x, int y, Bitmap bmp, int asteroidType) {
        super(bmp,asteroidType);

        reload = 1;
        health = 15 * reload;
        damage = 15;
        // for small asteroids
        width = bmp.getWidth()/6;
        height = bmp.getHeight()/4;

        srcX = (bmp.getWidth()*5)/6;
        srcY = rand.nextInt(asteroidType) * height;

        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        pos = new Rect(x, y, x+width, y+height);
    }
}

package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class SmallAsteroid extends Asteroid implements  GameObject {
    public SmallAsteroid(Rect pos, Bitmap bmp, int asteroidType) {
        super(pos, bmp, asteroidType);
    }
}

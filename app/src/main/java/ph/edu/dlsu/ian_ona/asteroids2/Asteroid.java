package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Asteroid implements GameObject {
    private Rect rectangle;
    private int color;

    public Asteroid(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
    }

    public boolean shipCollide (SpaceShip player) {

        if (    rectangle.contains(player.getRectangle().left, player.getRectangle().top) ||
                rectangle.contains(player.getRectangle().right, player.getRectangle().top) ||
                rectangle.contains(player.getRectangle().left, player.getRectangle().bottom) ||
                rectangle.contains(player.getRectangle().right, player.getRectangle().bottom)) {
            return true;

        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }
}

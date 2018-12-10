package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

public class Ammo implements  GameObject {
    private int height,width;
    private Bitmap bmp;
    private Rect src, pos;
    private int srcX, srcY;
    private int dmg;

    private final String TAG = Constants.getTAG(this);

    private boolean hit = false;

    public Ammo(Point p, Bitmap bmp) {
        this.bmp = bmp;

        // for normal ammo
        height = (int)(bmp.getHeight() * .09);
        width = (int)(bmp.getWidth() * .03);
        srcX = 0;
        srcY = 0;
        dmg = 15;

        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        pos = new Rect(p.x,p.y,p.x+width,p.y+height);
    }

    public Rect getPos() {
        return pos;
    }

    public int getDmg() {
        return dmg;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isHit() {
        return hit;
    }

    public void decrementY (float y) {
        pos.top -=y;
        pos.bottom -=y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bmp,src,pos,null);
        /*
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect(rectangle, paint);
        */
    }

    @Override
    public void update() {

    }
}

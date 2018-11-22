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

public class Asteroid implements GameObject {
    //private int x,y;
    private int height,width;
    private Bitmap bmp;
    private Rect src, pos;
    private int asteroidType;
    private int srcX, srcY;

    private final String TAG = Constants.getTAG(this);
    private Random rand = new Random();

    private boolean destroyed = false;
    private boolean exploded = false;
    private long explodeTime;

    public Asteroid(Rect pos, Bitmap bmp, int asteroidType) {
        this.pos = pos;
        this.bmp = bmp;

        // asteroidType determines size of asteroid
        // 1: big
        // 2: medium
        // 4: small (default)
        this.asteroidType = asteroidType;

        // for small asteroids
        height = bmp.getHeight()/4;
        width = bmp.getWidth()/6;
        srcX = (bmp.getWidth()*5)/6;
        srcY = rand.nextInt(asteroidType) * height;

        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public Rect getPos() {
        return pos;
    }

    public void incrementY (float y) {
        pos.top +=y;
        pos.bottom +=y;
        if (pos.top > Constants.SCREEN_HEIGHT)
            destroyed = true;
    }

    public boolean shipCollide (SpaceShip player) {
        return Rect.intersects(this.pos,player.getPos());
    }

    public boolean ammoCollide (Ammo shot) {
        if (!destroyed)
            return Rect.intersects(this.pos,shot.getPos());
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!destroyed)
            canvas.drawBitmap(bmp,src,pos,null);
    }

    @Override
    public void update() {

    }

    public void updateExplosion(){
        long elapsedTime = System.currentTimeMillis() - explodeTime;
        if (elapsedTime >= 100 && elapsedTime < 200){
            height = (int)(bmp.getHeight() * .15);
            width = (int)(bmp.getWidth() * .15);
            srcX = (int)(bmp.getWidth() * .07);
            srcY = (int)(bmp.getHeight() * .14);
            src = new Rect(srcX,srcY,srcX+ width,srcY+height);
            pos.set(pos.left,pos.top,pos.left+width,pos.top+height);
        } else if (elapsedTime >= 200 && elapsedTime < 300) {
            height = (int)(bmp.getHeight() * .21);
            width = (int)(bmp.getWidth() * .21);
            srcX = (int)(bmp.getWidth() * .22);
            srcY = (int)(bmp.getHeight() * .14);
            src = new Rect(srcX,srcY,srcX+ width,srcY+height);
            pos.set(pos.left,pos.top,pos.left+width,pos.top+height);
        } else if (elapsedTime >= 300){
            setDestroyed(true);
        }
    }

    public void explode(){
        bmp = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.ammo);
        height = (int)(bmp.getHeight() * .07);
        width = (int)(bmp.getWidth() * .07);
        srcX = 0;
        srcY = (int)(bmp.getHeight() * .14);
        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        pos.set(pos.left,pos.top,pos.left+width,pos.top+height);
        explodeTime = System.currentTimeMillis();
    }
}

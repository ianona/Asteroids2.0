package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public abstract class Asteroid implements GameObject {
    protected int height,width;
    protected Bitmap bmp;
    protected Rect src, pos;
    protected int asteroidType;
    protected int srcX, srcY;
    protected int health;
    protected int damage;

    protected final String TAG = Constants.getTAG(this);
    protected Random rand = new Random();

    protected boolean destroyed = false;
    protected boolean exploded = false;
    protected long explodeTime;
    protected float speed;

    protected Asteroid(Bitmap bmp, int asteroidType){
        this.bmp = bmp;

        // asteroidType determines size of asteroid
        // 1: big
        // 2: medium
        // 4: small (default)
        this.asteroidType = asteroidType;

        double prob = rand.nextDouble();
        // 30% chance to spawn slow asteroid
        if (prob < .30)
            speed = (float)6.0;
        // 70% chance to spawn fast asteroid
        else if (prob < .70)
            speed = (float)15.0;
        else
            speed = (float)8.0;
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

    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public void incrementY (float y) {
        pos.top +=y;
        pos.bottom +=y;
        if (pos.top > Constants.SCREEN_HEIGHT)
            destroyed = true;
    }

    public void incrementY () {
        pos.top += speed;
        pos.bottom += speed;
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

    @Override
    public void draw(Canvas canvas) {
        if (!destroyed)
            canvas.drawBitmap(bmp,src,pos,null);
    }

    @Override
    public void update() {

    }
}

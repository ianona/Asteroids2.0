package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class SpaceShip implements GameObject {
    private Bitmap bmp;
    GamePanel gamePanel;
    int height, width;
    int shipStatus = 1;
    int direction = 1;
    private Rect pos, src;
    private int health;
    private long dmgBuffer;

    private final String TAG = Constants.getTAG(this);

    public SpaceShip(GamePanel gamePanel, Bitmap bmp) {
        this.gamePanel = gamePanel;
        this.bmp = bmp;
        height = bmp.getHeight()/3;
        width = bmp.getWidth()/3;

        pos = new Rect(0,0,width,height);
        health = 100;
        dmgBuffer = 0;
    }

    public void reset(){
        health = 100;
        dmgBuffer = 0;
    }

    public Rect getPos() {
        return pos;
    }

    public int getHealth() {
        return health;
    }

    public void takeDmg(int damage) {
        this.health -= damage;
        dmgBuffer = System.currentTimeMillis();
    }

    public boolean isBuffered(){
        return System.currentTimeMillis() - dmgBuffer >= 1000;
    }

    public void setDmgBuffer(long dmgBuffer) {
        this.dmgBuffer = dmgBuffer;
    }

    @Override
    public void draw(Canvas canvas) {
        int srcY = shipStatus * height;
        int srcX = direction * width;
        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        Paint p = new Paint();
        if (!isBuffered())
            p.setAlpha(100);

        canvas.drawBitmap(bmp,src,pos,p);

        Rect healthBar = new Rect(0,Constants.SCREEN_HEIGHT-30,(int)((health/100.0) * Constants.SCREEN_WIDTH), Constants.SCREEN_HEIGHT);
        p.setColor(Color.GREEN);
        canvas.drawRect(healthBar,p);
        p = null;
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        pos.set(point.x - pos.width()/2, point.y - pos.height()/2, point.x + pos.width()/2, point.y + pos.height()/2);

        // shipStatus indicates what row of sprites we need to use for the ship
        // 0: ship stopping
        // 1: ship flying (default)
        // 2: ship slowing
        shipStatus = 1;

        // direction indicates what column of sprites we need to use for the ship
        // 0: ship is going left
        // 1: ship is centered (default)
        // 2: ship is going right
        direction = 1;
    }
}

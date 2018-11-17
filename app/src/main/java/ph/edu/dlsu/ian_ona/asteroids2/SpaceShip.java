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

    private final String TAG = Constants.getTAG(this);

    public SpaceShip(GamePanel gamePanel, Bitmap bmp) {
        this.gamePanel = gamePanel;
        this.bmp = bmp;
        height = bmp.getHeight()/3;
        width = bmp.getWidth()/3;

        pos = new Rect(0,0,width,height);
    }

    public Rect getPos() {
        return pos;
    }

    @Override
    public void draw(Canvas canvas) {
        int srcY = shipStatus * height;
        int srcX = direction * width;
        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        canvas.drawBitmap(bmp,src,pos,null);

        /*
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(rectangle, paint);
        */
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

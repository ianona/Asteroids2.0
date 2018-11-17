package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Background implements GameObject{
    private Bitmap bmp;
    GamePanel gamePanel;
    int height, width;
    private Rect pos, src;
    private int srcX, srcY;
    private long startTime, initTime;

    private final String TAG = Constants.getTAG(this);

    public Background(GamePanel gamePanel, Bitmap bmp) {
        this.gamePanel = gamePanel;
        this.bmp = bmp;
        height = bmp.getHeight()/3;
        width = bmp.getWidth();

        pos = new Rect(0,0,Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);
        srcX = 0;
        srcY = height * 2;

        startTime = initTime = System.currentTimeMillis();
    }

    public Rect getPos() {
        return pos;
    }

    @Override
    public void draw(Canvas canvas) {
        src = new Rect(srcX,srcY,srcX+ width,srcY+height);
        canvas.drawBitmap(bmp,src,pos,null);
    }

    @Override
    public void update() {
        float speed = height/15000.0f;
        srcY -= 2.5;
        if (srcY <= 0)
            srcY = height * 2;
    }
}

package ph.edu.dlsu.ian_ona.asteroids2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BmpButton implements GameObject {
    private Bitmap bmp;
    private Rect pos, src;

    public BmpButton(Bitmap bmp, int y) {
        this.bmp = bmp;

        src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        pos = new Rect((Constants.SCREEN_WIDTH/2)-(bmp.getWidth()/2), y, (Constants.SCREEN_WIDTH/2)-(bmp.getWidth()/2)+bmp.getWidth(), y+bmp.getHeight());
    }

    public Rect getPos() {
        return pos;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bmp,src,pos,null);
    }

    @Override
    public void update() {

    }
}

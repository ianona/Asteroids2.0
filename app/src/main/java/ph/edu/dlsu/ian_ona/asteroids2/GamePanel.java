package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;

    private SpaceShip player;
    private Point playerPoint;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);

        mainThread = new MainThread(getHolder(), this);

        player = new SpaceShip(new Rect(100,100,200,200), Color.BLUE);
        playerPoint = new Point();

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread = new MainThread(getHolder(), this);
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                playerPoint.set((int)event.getX(),(int)event.getY());
        }
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        player.update(playerPoint);
    }

    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
    }
}

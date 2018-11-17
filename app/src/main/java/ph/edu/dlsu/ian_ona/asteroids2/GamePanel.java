package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;

    private SpaceShip player;
    private Point playerPoint;
    private AsteroidManager asteroidManager;
    private Background bg;
    private AmmoManager ammoManager;

    private boolean movingShip = false;
    private boolean gameOver = false;
    private long gameOverTime;

    private MotionSensor motionSensor;
    private long frameTime;

    private final String TAG = Constants.getTAG(this);

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        Constants.CURRENT_CONTEXT = context;

        mainThread = new MainThread(getHolder(), this);

        player = new SpaceShip(this, BitmapFactory.decodeResource(getResources(),R.drawable.spaceship));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,5*Constants.SCREEN_HEIGHT/6);
        bg = new Background(this, BitmapFactory.decodeResource(getResources(),R.drawable.bg));

        asteroidManager = new AsteroidManager(500, BitmapFactory.decodeResource(getResources(),R.drawable.asteroids));
        ammoManager = new AmmoManager(BitmapFactory.decodeResource(getResources(),R.drawable.ammo));

        motionSensor = new MotionSensor();
        motionSensor.register();
        frameTime = System.currentTimeMillis();
        setFocusable(true);
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,5*Constants.SCREEN_HEIGHT/6);
        asteroidManager = new AsteroidManager(500, BitmapFactory.decodeResource(getResources(),R.drawable.asteroids));
        ammoManager = new AmmoManager(BitmapFactory.decodeResource(getResources(),R.drawable.ammo));
        movingShip = false;
        bg = new Background(this, BitmapFactory.decodeResource(getResources(),R.drawable.bg));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
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
                // allows for touch-controlled ship movement
                /*
                if (!gameOver && player.getPos().contains((int)event.getX(),(int)event.getY()))
                    movingShip = true;
                */
                if (gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    reset();
                    gameOver = false;
                    motionSensor.newGame();
                }

                // to do: code for shooting
                ammoManager.shoot(playerPoint.x,playerPoint.y);

                break;
            case MotionEvent.ACTION_MOVE:
                if (movingShip && !gameOver)
                    playerPoint.set((int)event.getX(),(int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingShip = false;
                break;
        }
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis()-frameTime);
            frameTime = System.currentTimeMillis();

            useMotionSensors(elapsedTime);

            bg.update();
            player.update(playerPoint);
            asteroidManager.update();
            ammoManager.update();
            for (Ammo a: ammoManager.getShots()){
                if (!a.isHit())
                    asteroidManager.ammoCollide(a);
            }
            if (asteroidManager.shipCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
            cleanUp();
        }
    }

    public void cleanUp(){
        for (int i = ammoManager.getShots().size()-1;i>=0;i--){
            if (ammoManager.getShots().get(i).isHit())
                ammoManager.getShots().remove(i);
        }

        for (int i = asteroidManager.getAsteroids().size()-1;i>=0;i--){
            if (asteroidManager.getAsteroids().get(i).isDestroyed())
                asteroidManager.getAsteroids().remove(i);
        }
    }

    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);
        //canvas.drawColor(Color.WHITE);
        bg.draw(canvas);
        if (gameOver) {
            canvas.drawColor(Color.BLACK);
        }
        player.draw(canvas);
        asteroidManager.draw(canvas);
        ammoManager.draw(canvas);
    }

    public void useMotionSensors(long elapsedTime){
        if (motionSensor.getOrientation() != null && motionSensor.getStartOrientation() != null) {
            float pitch = motionSensor.getOrientation()[1] - motionSensor.getStartOrientation()[1];
            float roll = motionSensor.getOrientation()[2] - motionSensor.getStartOrientation()[2];

            // tinker to modify speed caused by motion
            float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f;
            float ySpeed = pitch * Constants.SCREEN_HEIGHT / 1000f;

            playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
            playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
        }

        if (playerPoint.x < 0)
            playerPoint.x = 0;
        else if (playerPoint.x > Constants.SCREEN_WIDTH)
            playerPoint.x = Constants.SCREEN_WIDTH;
        if (playerPoint.y < 0)
            playerPoint.y = 0;
        else if (playerPoint.y > Constants.SCREEN_HEIGHT)
            playerPoint.y = Constants.SCREEN_HEIGHT;
    }
}

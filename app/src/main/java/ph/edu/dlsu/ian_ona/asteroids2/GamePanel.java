package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.*;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    private MainActivity mainMenu;
    private Rect textRect = new Rect();

    private SpaceShip player;
    private Point playerPoint;
    private AsteroidManager asteroidManager;
    private Background bg;
    private AmmoManager ammoManager;

    private boolean movingShip = false;
    private boolean gameOver = false;
    private boolean paused = false;
    private long gameOverTime;

    private MotionSensor motionSensor;
    private long frameTime;

    private BmpButton retryBtn;
    private BmpButton menuBtn;
    private BmpButton highBtn;
    private BmpButton resumeBtn;
    private boolean highScore = false;

    private final String TAG = Constants.getTAG(this);

    public GamePanel(Context context){
        super(context);
        this.mainMenu = (MainActivity)Constants.MAIN_CONTEXT;
        getHolder().addCallback(this);
        Constants.CURRENT_CONTEXT = context;

        mainThread = new MainThread(getHolder(), this);

        createSpaceship();
        createBG();
        asteroidManager = new AsteroidManager(BitmapFactory.decodeResource(getResources(),R.drawable.asteroids));
        ammoManager = new AmmoManager(BitmapFactory.decodeResource(getResources(),R.drawable.ammo));

        int btnHeight = BitmapFactory.decodeResource(getResources(),R.drawable.retrybtn).getHeight();
        retryBtn = new BmpButton(BitmapFactory.decodeResource(getResources(),R.drawable.retrybtn), (Constants.SCREEN_HEIGHT/2) + (1 * btnHeight));
        menuBtn = new BmpButton(BitmapFactory.decodeResource(getResources(),R.drawable.menubtn), (Constants.SCREEN_HEIGHT/2) + (2 * btnHeight));
        highBtn = new BmpButton(BitmapFactory.decodeResource(getResources(),R.drawable.savebtn), (Constants.SCREEN_HEIGHT/2) + (3 * btnHeight));
        resumeBtn = new BmpButton(BitmapFactory.decodeResource(getResources(),R.drawable.resumebtn), (Constants.SCREEN_HEIGHT/2) + (1 * btnHeight));

        motionSensor = new MotionSensor();
        motionSensor.register();
        frameTime = System.currentTimeMillis();
        setFocusable(true);
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,5*Constants.SCREEN_HEIGHT/6);
        player.reset();
        asteroidManager = new AsteroidManager(BitmapFactory.decodeResource(getResources(),R.drawable.asteroids));
        ammoManager = new AmmoManager(BitmapFactory.decodeResource(getResources(),R.drawable.ammo));
        movingShip = false;
        highScore = false;
        createBG();
    }

    public void createBG(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Constants.CURRENT_CONTEXT);

        switch (sharedPref.getInt(Constants.CURRENT_CONTEXT.getString(R.string.pref_background), R.string.map1)){
            case R.string.map1:
                bg = new Background(this, BitmapFactory.decodeResource(getResources(),R.drawable.bg));
                break;
            case R.string.map2:
                bg = new Background(this, BitmapFactory.decodeResource(getResources(),R.drawable.bg2));
                break;
            case R.string.map3:
                bg = new Background(this, BitmapFactory.decodeResource(getResources(),R.drawable.bg3));
                break;
        }

    }

    public void createSpaceship(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Constants.CURRENT_CONTEXT);

        playerPoint = new Point(Constants.SCREEN_WIDTH/2,5*Constants.SCREEN_HEIGHT/6);
        switch (sharedPref.getInt(Constants.CURRENT_CONTEXT.getString(R.string.pref_spaceship), R.string.space1)){
            case R.string.space1:
                player = new SpaceShip(this, BitmapFactory.decodeResource(getResources(),R.drawable.spaceship));
                break;
            case R.string.space2:
                player = new SpaceShip(this, BitmapFactory.decodeResource(getResources(),R.drawable.spaceship2));
                break;
            case R.string.space3:
                player = new SpaceShip(this, BitmapFactory.decodeResource(getResources(),R.drawable.spaceship3));
                break;
        }
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

    Handler mHandler;
    Runnable mAction = new Runnable() {
        @Override public void run() {
            if (!gameOver && asteroidManager.getScore() > 0){
                ammoManager.shoot(playerPoint.x,playerPoint.y-player.getPos().height()/2);
                asteroidManager.decrementScore();
            }
            mHandler.postDelayed(this, 250);
        }
    };

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // allows for touch-controlled ship movement
                /*
                if (!gameOver && player.getPos().contains((int)event.getX(),(int)event.getY()))
                    movingShip = true;
                */
                if (gameOver) {
                    if (retryBtn.getPos().contains((int)event.getX(),(int)event.getY())) {
                        gameOver = false;
                        reset();
                        motionSensor.newGame();
                    } else if (menuBtn.getPos().contains((int)event.getX(),(int)event.getY())) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MainActivity.musicService.stopMusic();
                        getContext().startActivity(intent);
                    }
                } else if (paused){
                    if (resumeBtn.getPos().contains((int)event.getX(),(int)event.getY())) {
                        paused = false;
                        asteroidManager.resume();
                        mainMenu.musicService.playMusic();
                    }
                } else {
                    if (asteroidManager.getScore() > 0){
                        ammoManager.shoot(playerPoint.x,playerPoint.y-player.getPos().height()/2);
                        asteroidManager.decrementScore();
                    }

                    if (mHandler != null) return true;
                    mHandler = new Handler();
                    mHandler.postDelayed(mAction, 250);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                /*
                if (movingShip && !gameOver)
                    playerPoint.set((int)event.getX(),(int)event.getY());
                */
                break;
            case MotionEvent.ACTION_UP:
                movingShip = false;
                if (mHandler == null) return true;
                mHandler.removeCallbacks(mAction);
                mHandler = null;
                break;
        }
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        if (!gameOver && !paused) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis()-frameTime);
            frameTime = System.currentTimeMillis();

            useMotionSensors(elapsedTime);

            bg.update();
            player.update(playerPoint);
            asteroidManager.update();
            ammoManager.update();

            // check for collisions between ammo and asteroid
            for (Ammo a: ammoManager.getShots()){
                if (!a.isHit())
                    asteroidManager.ammoCollide(a);
            }

            // check for collision between ship and asteroid
            if (asteroidManager.shipCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
                asteroidManager.setGameOver(true);
                if (mainMenu.updateHighScoreList(asteroidManager.getScore()))
                    highScore = true;
            }
            cleanUp();
        }
    }

    public void cleanUp(){
        for (int i = ammoManager.getShots().size()-1;i>=0;i--){
            if (ammoManager.getShots().get(i).isHit() || ammoManager.getShots().get(i).getPos().bottom < 0)
                ammoManager.getShots().remove(i);
        }

        for (int i = asteroidManager.getAsteroids().size()-1;i>=0;i--){
            if (asteroidManager.getAsteroids().get(i).isDestroyed() || asteroidManager.getAsteroids().get(i).getPos().top > Constants.SCREEN_HEIGHT)
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
        asteroidManager.draw(canvas);
        player.draw(canvas);
        ammoManager.draw(canvas);

        Paint p = new Paint();

        if (paused) {
            canvas.drawColor(Color.argb(200,0,0,0));
            resumeBtn.draw(canvas);
        }

        if (gameOver) {
            p.setTextSize(200);
            p.setColor(Color.WHITE);
            p.setTypeface(Constants.PIXEL_FONT);
            drawCenterText(canvas, p, "Score: "+ asteroidManager.getScore());

            // draw buttons here
            retryBtn.draw(canvas);
            menuBtn.draw(canvas);
            if (highScore)
                highBtn.draw(canvas);
        }
        p = null;
    }

    public void pause(){
        if (!gameOver){
            mainMenu.musicService.pauseMusic();
            paused = true;
            asteroidManager.pause();
        }
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

        if (playerPoint.x < 0 + player.getPos().width()/2)
            playerPoint.x = 0 + player.getPos().width()/2;
        else if (playerPoint.x > Constants.SCREEN_WIDTH - player.getPos().width()/2)
            playerPoint.x = Constants.SCREEN_WIDTH - player.getPos().width()/2;
        if (playerPoint.y < 0)
            playerPoint.y = 0;
        else if (playerPoint.y > Constants.SCREEN_HEIGHT - player.getPos().height()/2 - 30)
            playerPoint.y = Constants.SCREEN_HEIGHT - player.getPos().height()/2 - 30;
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(textRect);
        int cHeight = textRect.height();
        int cWidth = textRect.width();
        paint.getTextBounds(text, 0, text.length(), textRect);
        float x = cWidth / 2f - textRect.width() / 2f - textRect.left;
        float y = cHeight / 2f + textRect.height() / 2f - textRect.bottom;
        canvas.drawText(text, x, y, paint);
    }


}

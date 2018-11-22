package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MotionSensor implements SensorEventListener{
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    private float[] accelOutput;
    private float[] magOutput;

    private float[] orientation = new float[3];
    private float[] startOrientation = null;

    private final String TAG = Constants.getTAG(this);

    public MotionSensor(){
        manager = (SensorManager) Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void register(){
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause(){
        manager.unregisterListener(this);
    }

    public float[] getOrientation() {
        return orientation;
    }

    public float[] getStartOrientation() {
        return startOrientation;
    }

    public void newGame(){
        startOrientation = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelOutput = event.values;
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magOutput = event.values;

        if (accelOutput != null && magOutput != null){
            float[] RMatrix = new float[9];
            float[] IMatrix = new float[9];
            boolean success = SensorManager.getRotationMatrix(RMatrix, IMatrix, accelOutput, magOutput);
            if (success) {
                SensorManager.getOrientation(RMatrix, orientation);
                if (startOrientation == null) {
                    startOrientation = new float[orientation.length];

                    // use this code for auto-adjust to phone-position
                    /*
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);
                    for (int i=0;i<orientation.length;i++){
                        Log.d(TAG,"ORIENTATION: "+orientation[i]);
                    }
                    */

                    // flat-down phone position
                    orientation[0] = (float)-2.1787436;
                    orientation[1] = (float)-0.06648044;
                    orientation[2] = (float)0.01628869;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

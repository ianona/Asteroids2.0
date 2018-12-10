package ph.edu.dlsu.ian_ona.asteroids2;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
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

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Constants.CURRENT_CONTEXT);
                    switch (sharedPref.getInt(Constants.CURRENT_CONTEXT.getString(R.string.pref_motion), R.string.tilt1)){
                        case R.string.tilt1:
                            // regular phone position
                            Log.d(TAG,"REGULAR");
                            startOrientation[0] = (float)-2.720837;
                            startOrientation[1] = (float)-0.73023206;
                            startOrientation[2] = (float)-0.004654862;
                            break;
                        case R.string.tilt2:
                            // flat-down phone position
                            Log.d(TAG,"TOP-DOWN");
                            startOrientation[0] = (float)-2.1787436;
                            startOrientation[1] = (float)-0.06648044;
                            startOrientation[2] = (float)0.01628869;
                            break;
                        case R.string.tilt3:
                            // use this code for auto-adjust to phone-position
                            Log.d(TAG,"AUTO");
                            System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);
                            break;
                    }

                    for (int i=0;i<startOrientation.length;i++){
                        Log.d(TAG,"START ORIENTATION: "+startOrientation[i]);
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

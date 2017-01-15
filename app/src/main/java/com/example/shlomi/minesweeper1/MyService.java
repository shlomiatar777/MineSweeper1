package com.example.shlomi.minesweeper1;



/**
 * Created by Shlomi on 15/01/2017.
 */



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;


public class MyService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    static boolean first = true;
    private float firstx,firsty,firstz;
    private float x,y,z;
    public static final String BROADCAST_ACTION = "Logic";
    private final Handler handler = new Handler();
    Intent intent;


    public MyService() {
    }

    @Override//
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //registering Sensor
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

        //then you should return sticky
        return Service.START_STICKY;
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 5000); // 5 seconds
        }
    };


    private void DisplayLoggingInfo() {
        intent.putExtra("firstx", getFirstx());
        intent.putExtra("firsty", getFirsty());
        intent.putExtra("firstz", getFirstz());
        intent.putExtra("x", getX());
        intent.putExtra("y", getY());
        intent.putExtra("z", getZ());
        sendBroadcast(intent);
    }


    @Override
    public void onCreate() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        intent = new Intent(BROADCAST_ACTION);
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        if (first){
            firstx = +se.values[0];
            firsty = +se.values[1];
            firstz = +se.values[2];
            first = false;
        }
        x = +se.values[0];
        y = +se.values[1];
        z = +se.values[2];



    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float getFirstx() {
        return firstx;
    }

    public float getFirsty() {
        return firsty;
    }

    public float getFirstz() {
        return firstz;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}

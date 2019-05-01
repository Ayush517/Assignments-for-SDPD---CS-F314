package com.example.hw3_2015a7ps0010g;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class AccelerationService extends Service {

    private ArrayList<Data> dq;
    public CountDownTimer ct;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double acceleration;
    private String startTime;
    private String endTime;
    private double currentAcceleration;
    private Timer t;
    private SensorEventListener listen;

    public AccelerationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        Toast.makeText(this, "service started", Toast.LENGTH_LONG).show();
        String value1 = "start";
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listen = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                double x = sensorEvent.values[0];
                double y = sensorEvent.values[1];
                double z = sensorEvent.values[2];
                currentAcceleration = Math.sqrt((x*x)+(y*y)+(z*z));
                //System.out.println(currentAcceleration);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(listen, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        startTime = sdf.format(Calendar.getInstance().getTime());
        dq = new ArrayList<Data>();
        t = new Timer();
        TimerTask tsk = new TimerTask() {
            @Override
            public void run() {
                if(dq.size() == 600) {
                    dq.clear();
                }
                dq.add(new Data(new Date(), currentAcceleration));
                sendGraphData(dq);
            }
        };
        t.scheduleAtFixedRate(tsk, 0, 1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.cancel();
        sensorManager.unregisterListener(listen, accelerometer);
        /*for (Data d: dq) {
            //System.out.println(d.time + " " + d.acceleration);
        }*/
        Toast.makeText(this, "service stopped", Toast.LENGTH_LONG).show();
    }

    private void sendGraphData(ArrayList<Data> dq) {
        Intent intent = new Intent("realtimegraph");
        Bundle b = new Bundle();
        b.putParcelableArrayList("data", dq);
        intent.putExtra("data", b);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

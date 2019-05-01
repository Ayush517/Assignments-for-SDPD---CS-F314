package com.example.hw3_2015a7ps0010g;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean toggleSensor(boolean isChecked) {
        if (!isChecked) {
            stopAccelerationService();
        } else {
            startAccelerationService();
        }
        return true;
    }

    public void startAccelerationService() {
        Intent it = new Intent(this, AccelerationService.class);
        it.setAction("start");
        startService(it);
    }

    public void stopAccelerationService() {
        Intent it = new Intent(this, AccelerationService.class);
        stopService(it);
    }

    public void showGraph() {
        Intent in = new Intent(MainActivity.this, graph.class);
        startActivity(in);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //System.out.println("onSaveInstanceState " + isSampling);
        //outState.putBoolean("isSampling", isSampling);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //System.out.println("onRestoreInstance " + savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        //System.out.println("OnPause " + isSampling);
    }

    @Override
    public void onResume() {
        super.onResume();
        //System.out.println("OnResume " + isSampling);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

package com.example.hw5_2017a8ps0604g;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    public OutputStream outputStream;
    public InputStream inStream;
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enableBluetoothIfDisabled();
        Intent it = new Intent(this, BluetoothService.class);
        startService(it);
    }

    public void enableBluetoothIfDisabled() {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled())
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth service not available", Toast.LENGTH_SHORT);
        }
    }

    public void turnOnWifi(View view) throws IOException {
        this.connectIfInRange();
        this.sendStatus();
    }

    public void connectIfInRange() throws IOException {

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        BluetoothDevice device = null;
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice bd : bondedDevices) {
                if (bd.getName().equals("ESP32 BT")) {
                    device = bd;
                    break;
                }
            }
            ParcelUuid[] uuids = device.getUuids();
            BluetoothSocket socket = null;
            try {
                socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            } catch (Exception e) {
                Log.e("", "Error creating socket");
            }

            try {
                socket.connect();
                Log.e("", "Connected");
            } catch (IOException e) {
                Log.e("", e.getMessage());
                try {
                    Log.e("", "trying fallback...");

                    socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                    socket.connect();

                    Log.e("", "Connected");
                } catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }
            //socket.connect();
            //socket.
            outputStream = socket.getOutputStream();
            inStream = socket.getInputStream();
        }
    }


    public void sendStatus() throws IOException {
        this.outputStream.write(22);
    }

    private void init() throws IOException {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {


                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);

            }
        }
    }

    public void write(String s) throws IOException {
        this.outputStream.write(s.getBytes());
    }

    public void run() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;
    }
}

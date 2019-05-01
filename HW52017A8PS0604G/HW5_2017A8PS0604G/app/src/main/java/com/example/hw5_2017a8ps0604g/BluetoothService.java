package com.example.hw5_2017a8ps0604g;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class BluetoothService extends Service {
    public OutputStream outputStream;
    public InputStream inStream;
    BluetoothSocket socket = null;
    public Timer t;
    public TimerTask tsk;

    public BluetoothService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        // TODO: Return the communication channel to the service.
        Toast.makeText(this, "service started", Toast.LENGTH_LONG).show();
        t = new Timer();
        tsk = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (deviceFound()) {
                        sendStatus();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.scheduleAtFixedRate(tsk, 0, 100);
        return START_STICKY;
    }

    public boolean deviceFound() throws IOException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        BluetoothDevice device = null;
        if(bondedDevices.size() > 0) {
            for (BluetoothDevice bd : bondedDevices) {
                if (bd.getName().equals("ESP32 BT")) {
                    device = bd;
                    ParcelUuid[] uuids = device.getUuids();
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
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                    return true;
                }
            }
        }
        return false;
    }

    public void sendStatus() throws IOException {
        Context c = getBaseContext();
        BatteryManager bm = (BatteryManager)getApplicationContext().getSystemService(getApplicationContext().BATTERY_SERVICE);
        System.out.println(BatteryManager.ACTION_CHARGING);
        final Intent batteryIntent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;
        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if (batteryCharge || usbCharge || acCharge) {
            this.outputStream.write(0);
            System.out.println("Charging");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.cancel();
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (ba != null && ba.isEnabled())
            ba.disable();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

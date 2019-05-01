package com.HW22017A8PS0604G;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public void onStart() {
        super.onStart();
        Log.v("testing", "onStart was called! Main Activity started");
    }

    public static final String EXTRA_MESSAGE = "com.HW22017A8PS0604G.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("testing", "onCreate was called! Application CREATED");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.v("testing", "onResume was called! Main Activity resumed and RUNNING");

    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.v("testing", "onPause was called! Main Activity PAUSED");
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        Log.v("testing", "onRestart was called! Main Activity restarted");
    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        Log.v("testing", "onStop was called! Main Activity STOPPED");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.v("testing", "onDestroy was called! Main Activity destroyed");
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Log.v("testing", "sendMessage was called! Main Activity sent message to Secondary Activity");
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}

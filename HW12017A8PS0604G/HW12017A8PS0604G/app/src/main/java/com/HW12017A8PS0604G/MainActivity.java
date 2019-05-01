package com.HW12017A8PS0604G;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void perform_action(View v)
    {
        Button btn=(Button) findViewById(R.id.push_button);
        if(i%2==0)
            btn.setText("1");
        else
            btn.setText("0");
        i++;
    }
}

package com.example.hw3_2015a7ps0010g;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class sensor extends Fragment {

    public sensor() {
        // Required empty public constructor
    }

    boolean isSampling = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        final Button button = (Button)view.findViewById(R.id.sensor);
        final ToggleButton tb = (ToggleButton)view.findViewById(R.id.toggleButton);
        final Button btn = (Button)view.findViewById(R.id.plot);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGraph();
            }
        });
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                toggleSensor(isChecked);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton tb = (ToggleButton)getActivity().findViewById(R.id.toggleButton);
                if (tb.getVisibility() == View.VISIBLE)
                    tb.setVisibility(View.GONE);
                else {
                    tb.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    public void toggleSensor(boolean isChecked) {

        MainActivity act = (MainActivity)getActivity();
        act.toggleSensor(isChecked);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSampling", isSampling);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated " + isSampling);
        if (savedInstanceState != null){
            isSampling = savedInstanceState.getBoolean("isSampling");
        }
    }

    public void showGraph() {
        ToggleButton tb = (ToggleButton)getActivity().findViewById(R.id.toggleButton);
        if(tb.isChecked()) {
            MainActivity act = (MainActivity) getActivity();
            act.showGraph();
        } else {
            Toast.makeText(getContext(), "Please Enable Accelerometer Sensor", Toast.LENGTH_LONG).show();
        }
    }
}

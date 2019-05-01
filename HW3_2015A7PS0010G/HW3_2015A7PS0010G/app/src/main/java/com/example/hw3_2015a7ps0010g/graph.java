package com.example.hw3_2015a7ps0010g;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class graph extends AppCompatActivity {

    public long minTime;
    public long maxTime;
    public LineGraphSeries<DataPoint> series;
    public GraphView graph;

    public BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("data");
            ArrayList<Data> dq = b.getParcelableArrayList("data");
            minTime = dq.get(0).time.getTime();
            maxTime = minTime+600000L;
            //System.out.println(minTime + " " + maxTime + " " + (maxTime-minTime));
            DataPoint[] dp = new DataPoint[dq.size()];
            for (int i=0; i<dq.size(); i++) {
                dp[i] = new DataPoint(dq.get(i).time, dq.get(i).acceleration);
            }
            graph.getViewport().setMinX(minTime);
            graph.getViewport().setMaxX(maxTime);;
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getGridLabelRenderer().setHighlightZeroLines(true);
            graph.getGridLabelRenderer().setGridColor(Color.rgb(0, 0, 0));
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
            series.resetData(dp);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        LocalBroadcastManager.getInstance(this).registerReceiver(dataReceiver, new IntentFilter("realtimegraph"));;
        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(new Date(), 9.8)
        });
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX)
                    return sdf.format(new Date((long)value));
                return super.formatLabel(value, isValueX);
            }
        });
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setGridColor(Color.rgb(0,0,0));
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Acceleration");
    }
}

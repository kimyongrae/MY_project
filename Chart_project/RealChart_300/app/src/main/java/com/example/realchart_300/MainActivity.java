package com.example.realchart_300;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int DATE_SIZE=20;
    ArrayList<Entry> dataVals=new ArrayList<>();

    LineChart mpLineChart;

    LineDataSet lineDataSet1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mpLineChart=(LineChart)findViewById(R.id.line_chart);
        mpLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        mpLineChart.setAutoScaleMinMaxEnabled(true);


        for (int i=0; i<300; i++){
            int a = 0;
            a = (int)(Math.random()*300);
            dataVals.add(new Entry(i,a));
        }

        lineDataSet1=new LineDataSet(dataVals,"Data set1");

        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setCircleColor(Color.BLUE);
        lineDataSet1.setColors(Color.RED);


        XAxis xAxis = mpLineChart.getXAxis();

        ArrayList<ILineDataSet>dataSets=new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data=new LineData(dataSets);
        data.setDrawValues(true);
        data.setValueTextColor(Color.BLACK);

        mpLineChart.setData(data);
        mpLineChart.invalidate();

//깃허브 짱이다...

    }
}

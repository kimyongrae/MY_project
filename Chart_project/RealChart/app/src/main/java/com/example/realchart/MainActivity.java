package com.example.realchart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    public static final int DATE_SIZE=2000;
    ArrayList<Entry>dataVals=new ArrayList<>();
    ArrayList<Entry>dataVals2=new ArrayList<>();

    LineChart mpLineChart;

    LineDataSet lineDataSet1;
    LineDataSet lineDataSet2;
    XAxis xAxis;
    static float position=0;
    boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mpLineChart=(LineChart)findViewById(R.id.line_chart);
        mpLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        mpLineChart.setAutoScaleMinMaxEnabled(true);

//        mpLineChart.getLegend().setEnabled(false);// 라벨값


        dataVals.add(new Entry(0,0));
        dataVals2.add(new Entry(0,0));


        lineDataSet1=new LineDataSet(dataVals,"Data set1");
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setLineWidth(3f);

        lineDataSet2=new LineDataSet(dataVals2,"Data set2");
        lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet2.setColor(Color.RED);
        lineDataSet2.setCircleColor(Color.BLACK);



        xAxis = mpLineChart.getXAxis();

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            //private final SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd", Locale.KOREA);
            private SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm");
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date();

//              long millis = date.getTime() + TimeUnit.HOURS.toMillis((long) value);
//              long millis=date.getTime()+1000*60*60*(long)value;
//                return mFormat.format(new Date(millis));
                return  ""+value;
            }
        });

        ArrayList<ILineDataSet>dataSets=new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);

        LineData data=new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();

        threadStart();

    }


    public void chartUpdate(int x,int b) {

        if(flag==true){
            flag=false;
            position=position+1;
        }

        if (dataVals.size() >= DATE_SIZE) {
            dataVals.remove(0);
        }

        if(dataVals2.size()>=DATE_SIZE){
            dataVals2.remove(0);
        }

        if(position==3000){
            position=0;
            dataVals.clear();
            dataVals2.clear();
        }
        position++;
        for (int i=0; i<300; i++){
            dataVals.add(new Entry(position, b));
            Log.d("tag222",b+"");
        }

//        dataVals2.add(new Entry(position,b));

        Log.d("positon","positon:"+position+","+"X:"+x+"dataVals.size():"+dataVals.size());

       /* xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd", Locale.KOREA);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.d("value","값:"+value);
                Date date = new Date();

//                long millis = date.getTime() + TimeUnit.HOURS.toMillis((long) value);
                long millis=date.getTime()+1000*60*60*(long)value;
                return mFormat.format(new Date(millis));
            }
        });*/

        lineDataSet1.notifyDataSetChanged();
        lineDataSet2.notifyDataSetChanged();
        mpLineChart.notifyDataSetChanged();
        mpLineChart.invalidate();
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            if(msg.what == 0){ // Message id 가 0 이면
          /*      int a = 0;
                a = (int)(Math.random()*100);*/
                int b= 0;
                b = (int)(Math.random()*100);
//                    chartUpdate(a,b);
                if(flag==true){
                    flag=false;
                    position=position+1;
                }

                if (dataVals.size() >= DATE_SIZE) {
                    dataVals.remove(0);
                }

                if(dataVals2.size()>=DATE_SIZE){
                    dataVals2.remove(0);
                }

                if(position==3000){
                    position=0;
                    dataVals.clear();
                    dataVals2.clear();
                }
                position++;

                for (int i=0; i<300; i++){
                    dataVals.add(new Entry(position, b));
                    Log.d("tag222","i의값:"+i+"");
                }

//        dataVals2.add(new Entry(position,b));


       /* xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("MM-dd", Locale.KOREA);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.d("value","값:"+value);
                Date date = new Date();

//                long millis = date.getTime() + TimeUnit.HOURS.toMillis((long) value);
                long millis=date.getTime()+1000*60*60*(long)value;
                return mFormat.format(new Date(millis));
            }
        });*/

                lineDataSet1.notifyDataSetChanged();
                lineDataSet2.notifyDataSetChanged();
                mpLineChart.notifyDataSetChanged();
                mpLineChart.invalidate();
            }
        }
    };


    class MyThread extends Thread{
        @Override
        public void run() {
            while(true){
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void threadStart() {
        MyThread thread = new MyThread();
        thread.setDaemon(true);
        thread.start();
    }
}




package com.example.bluetoothtest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.xml.sax.Parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    public static final int DATE_SIZE=40;
    ArrayList<Entry>dataVals=new ArrayList<>();
    ArrayList<Entry>dataVals2=new ArrayList<>();

    LineChart mpLineChart;

    LineDataSet lineDataSet1;
    LineDataSet lineDataSet2;
    XAxis xAxis;
    static float position=0;
    boolean flag=true;



    Button listen,send,listDevice;
    ListView listView;
    TextView msg_box,status;
    EditText writeMsg;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;

    SendRecive sendRecive;

    static final int STATE_LISTENING=1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    int REQUEST_ENABLE_BLUETOOTH=1;

    private static final String APP_NAME = "Bluetooth";

    private static final UUID MY_UUID = UUID.fromString("42bdd295-604a-4072-8260-abdee9fb4a11");
    //private static final UUID MY_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByIdes();

        mpLineChart=(LineChart)findViewById(R.id.line_chart);
        mpLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        mpLineChart.setAutoScaleMinMaxEnabled(true);

//        mpLineChart.getLegend().setEnabled(false);// 라벨값


        dataVals.add(new Entry(0,0));


        lineDataSet1=new LineDataSet(dataVals,"Data set1");
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setLineWidth(3f);

        xAxis = mpLineChart.getXAxis();

        /*xAxis.setValueFormatter(new IAxisValueFormatter() {

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
        });*/

        ArrayList<ILineDataSet>dataSets=new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data=new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();


        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

      /*  ServerClass serverClass=new ServerClass();
        serverClass.start();*/

        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        }

        implementListeners();
    }

    private void implementListeners(){

        listDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                String [] strings=new String[bt.size()];
                btArray=new BluetoothDevice[bt.size()];

                int index=0;

                if(bt.size()>0){

                    for(BluetoothDevice device : bt)
                    {
                        btArray[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }

            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerClass serverClass=new ServerClass();
                serverClass.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ClientClass clientClass=new ClientClass(btArray[position]);
                    clientClass.start();

                    status.setText("Connecting");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string=String.valueOf(writeMsg.getText());
                sendRecive.write(string.getBytes());
            }
        });

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case STATE_LISTENING:
                      status.setText("ListenLing");
                      break;
                case STATE_CONNECTING:
                      status.setText("Connecting(연결중)");
                      break;
                case STATE_CONNECTED:
                      status.setText("connected(연결됨)");
                      break;
                case STATE_CONNECTION_FAILED:
                     status.setText("Connection Failed(연결실패)");
                     break;
                case STATE_MESSAGE_RECEIVED:
                    byte [] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff,0,msg.arg1);
                    msg_box.setText(tempMsg);

                    int b= Integer.parseInt(tempMsg);

                    if(flag==true){
                        flag=false;
                        position=position+1;
                    }

                    if (dataVals.size() >= DATE_SIZE) {
                        dataVals.remove(0);
                    }

                    if(position==300){
                        position=0;
                        dataVals.clear();
                    }
                    position++;
                    dataVals.add(new Entry(position, b));

                    lineDataSet1.notifyDataSetChanged();
                    mpLineChart.notifyDataSetChanged();
                    mpLineChart.invalidate();

                    break;
            }
            return true;
        }
    });


    private void findViewByIdes(){
        listen=(Button)findViewById(R.id.Listen);
        send=(Button)findViewById(R.id.send);
        listView=(ListView) findViewById(R.id.listview);
        msg_box=(TextView)findViewById(R.id.message);
        status=(TextView)findViewById(R.id.status);
        writeMsg=(EditText) findViewById(R.id.writemsg);
        listDevice=(Button)findViewById(R.id.ListDevices);
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){

            try {
                serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            }catch (IOException e){
                e.printStackTrace();
            }

        }

        @Override
        public void run()
        {
            BluetoothSocket socket = null;

            while (socket==null)
            {
                try {

                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                }catch (IOException e){
                    e.printStackTrace();

                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);

                }

                if(socket!=null)
                {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    //write some code for send / receive
                    sendRecive=new SendRecive(socket);
                    sendRecive.start();

                    break;
                }

            }
        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass (BluetoothDevice device1){

            this.device=device1;

            try {
                socket=device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            try {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendRecive=new SendRecive(socket);
                sendRecive.start();

            }catch (IOException e){
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }

        }

    }

    private class SendRecive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
//        private final Reader reader;
        private final OutputStream outputStream;
        BufferedInputStream bufferedInputStream;

        public SendRecive (BluetoothSocket socket){
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();

                tempOut=bluetoothSocket.getOutputStream();
            }catch (IOException e){
                e.printStackTrace();
            }

//            reader=new InputStreamReader(tempIn);
            inputStream=tempIn;
            outputStream=tempOut;

        }

        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true)
            {
                try{
                    bytes=inputStream.read(buffer);
                    if(bytes!=-1){
                        handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

            }

        }

        public void write(byte[] bytes)
        {
            try{
                outputStream.write(bytes);
            } catch (IOException e){
                e.printStackTrace();
            }
        }



    }

}

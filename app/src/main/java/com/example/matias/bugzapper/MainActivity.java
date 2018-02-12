package com.example.matias.bugzapper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends Activity  {
    Button b1,b2,b3,b4,b5,b6;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    ListView lv;
    private OutputStream outputStream;
    private InputStream inputStream;

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);
        b5=(Button)findViewById(R.id.button5);
        b6=(Button)findViewById(R.id.button6);

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listView);
    }

    public void on(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void ZapperON (View v) throws IOException {
       if (BA.isEnabled()) {
           //TODO: send 1 to arduino
           write("1");
           Toast.makeText(getApplicationContext(), "Just sent 1", Toast.LENGTH_LONG).show();
       } else {
           Toast.makeText(getApplicationContext(), "Bluetooth off", Toast.LENGTH_LONG).show();
       }
    }

    public void ZapperOFF (View v) throws IOException{
        if (BA.isEnabled()) {
            //TODO: send 0 to arduino
            write("0");
            Toast.makeText(getApplicationContext(), "Just sent 0", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth off", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v){
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }


    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void list(View v) throws IOException{
        pairedDevices = BA.getBondedDevices();
        if(pairedDevices.size() > 0) {
            Object[] devices = (Object []) pairedDevices.toArray();
            BluetoothDevice device = (BluetoothDevice) devices[0];
            ParcelUuid[] uuids = device.getUuids();
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            socket.connect();
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        }
        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }
}

package com.example.catapult_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

public class MotorControl extends AppCompatActivity {
    // Button from UI
    Button full_a, half_a, release_a, disarm, disconnect, block;
    String address = null;
    // variable
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//attention peut être à changer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view
        setContentView(R.layout.activity_motor_control);

        //fetching extra
        Intent newInt = getIntent();
        address = newInt.getStringExtra(MainActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //calling buttons
        full_a = (Button)findViewById(R.id.button_full_armed);
        half_a = (Button)findViewById(R.id.button_half_armed);
        release_a = (Button)findViewById(R.id.button_release_arm);
        disarm= (Button)findViewById(R.id.button_disarm);
        disconnect=(Button)findViewById(R.id.button_disconnect);
        block=(Button)findViewById(R.id.button_block);

        //start BT connection
        new ConnectBT().execute(); //Call the class to connect

        //Set up of listener
        full_a.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                f_fullArmed();      //method to turn on
            }
        });

        half_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                f_halfArmed();   //method to turn off
            }
        });

        release_a.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                f_Release(); //close connection
            }
        });
        disarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                f_Disarm();      //method to turn on
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect();    //close connection
            }
        });
        block.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                f_Block();    //close connection
            }
        });
    }

    //functions to be done for each button
    private void Disconnect()//disconnect from BT
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {
                Log.d("Disconnect_b","Error");
            }
        }
        finish(); //return to the first layout

    }
    private void f_fullArmed()//completely arm the catapult
    {
        if (btSocket!=null)
        {
            try
            {
                Log.d("fullarmed_b","launch_1");
                btSocket.getOutputStream().write("1".getBytes());
                Log.d("fullarmed_b","launch");
            }
            catch (IOException e)
            {
                Log.d("fullarmed_b","Error");
            }
        }
    }

    private void f_halfArmed()// half arm the catapult
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("2".getBytes());
                Log.d("halfarmed_b","launch");
            }
            catch (IOException e)
            {
                Log.d("halfArmed_b","Error");
            }
        }
    }
    private void f_Release()//
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("0".getBytes());
            }
            catch (IOException e)
            {
                Log.d("release_b","Error");
            }
        }
    }

    private void f_Disarm()
    {
        if (btSocket!=null)
        {
            try
            {
                Log.d("disarmed_b","launch_1");
                btSocket.getOutputStream().write("3".getBytes());
            }
            catch (IOException e)
            {
                Log.d("disarm_b","Error");
            }
        }
    }

    private void f_Block()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("4".getBytes());
            }
            catch (IOException e)
            {
                Log.d("block","Error");
            }
        }
    }

    //asynchronous class to connect to bluetooth in background
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MotorControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositiv = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositiv.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Log.d("BTConnect","Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                Log.d("BTConnect","Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}


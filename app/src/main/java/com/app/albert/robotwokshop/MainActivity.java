package com.app.albert.robotwokshop;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.os.Handler;
import android.view.View;
import android.view.View.*;
import android.view.*;
import android.util.Log;


public class MainActivity extends ActionBarActivity {

    private BluetoothArduino mBlue;
    private Button mMove_Forward_Btn;
    private Button mMove_Backward_Btn;
    private Button mMove_Left_Btn;
    private Button mMove_Right_Btn;
    private Button mStart_Counterclockwise_Btn;
    private Button mStart_Clockwise_Btn;
    private Button mStart_DC_Btn;

    private TextView mMessagebox ;

    private int mCmd;
    private int mFRightSpeed;
    private int mBRightSpeed;
    private int mFLeftSpeed;
    private int mBLeftSpeed;
    private int mServoAngle;
    private int mDCMotor;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCmd = 0;
        mFRightSpeed = 0;
        mBRightSpeed = 0;
        mFLeftSpeed = 0;
        mBLeftSpeed = 0;
        mDCMotor = 0;

        mServoAngle = 0;

        mMessagebox = (TextView)findViewById(R.id.Messagebox);

        mHandler = new Handler();

        mBlue = BluetoothArduino.getInstance("HC-05");
        if(mBlue.Connect() == true)
        {
            mMessagebox.setText("Bluetooth : connect");
        }
        else
        {
            mMessagebox.setText("Bluetooth : disconnect");
        }


        mMove_Forward_Btn = (Button)findViewById(R.id.Move_Forward);
        mMove_Forward_Btn.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mMove_Backward_Btn.setEnabled(false);
                    mCmd = 1;
                    mFRightSpeed = 255;
                    mBRightSpeed = 255;
                    mFLeftSpeed = 255;
                    mBLeftSpeed = 255;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mCmd = 0;
                    mFRightSpeed = 0;
                    mBRightSpeed = 0;
                    mFLeftSpeed = 0;
                    mBLeftSpeed = 0;
                    SendCommand();
                    mMove_Backward_Btn.setEnabled(true);
                }
                return false;
            }
        });

        mMove_Backward_Btn = (Button)findViewById(R.id.Move_Backward);
        mMove_Backward_Btn.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mMove_Forward_Btn.setEnabled(false);
                    mCmd = 1;
                    mFRightSpeed = -255;
                    mBRightSpeed = -255;
                    mFLeftSpeed = -255;
                    mBLeftSpeed = -255;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mCmd = 0;
                    mFRightSpeed = 0;
                    mBRightSpeed = 0;
                    mFLeftSpeed = 0;
                    mBLeftSpeed = 0;
                    SendCommand();
                    mMove_Forward_Btn.setEnabled(true);
                }
                return false;
            }
        });

        mMove_Left_Btn = (Button)findViewById(R.id.Move_Left);
        mMove_Left_Btn.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mMove_Right_Btn.setEnabled(false);
                    mCmd = 1;
                    mFRightSpeed = 255;
                    mBRightSpeed = 255;
                    mFLeftSpeed = 0;
                    mBLeftSpeed = 0;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mCmd = 0;
                    mFRightSpeed = 0;
                    mBRightSpeed = 0;
                    mFLeftSpeed = 0;
                    mBLeftSpeed = 0;
                    SendCommand();
                    mMove_Right_Btn.setEnabled(true);
                }
                return false;
            }
        });

        mMove_Right_Btn = (Button)findViewById(R.id.Move_Right);
        mMove_Right_Btn.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mMove_Left_Btn.setEnabled(false);
                    mCmd = 1;
                    mFRightSpeed = 0;
                    mBRightSpeed = 0;
                    mFLeftSpeed = 255;
                    mBLeftSpeed = 255;

                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mCmd = 0;
                    mFRightSpeed = 0;
                    mBRightSpeed = 0;
                    mFLeftSpeed = 0;
                    mBLeftSpeed = 0;
                    SendCommand();
                    mMove_Left_Btn.setEnabled(true);
                }
                return false;
            }
        });


        mStart_Counterclockwise_Btn = (Button)findViewById(R.id.Start_Counterclockwise);
        mStart_Counterclockwise_Btn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mStart_Clockwise_Btn.setEnabled(false);
                    mServoAngle = 180;

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mStart_Clockwise_Btn.setEnabled(true);
                }
                return false;
            }
        });

        mStart_Clockwise_Btn = (Button)findViewById(R.id.Start_Clockwise);
        mStart_Clockwise_Btn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mStart_Counterclockwise_Btn.setEnabled(false);
                    mServoAngle = 0;

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mStart_Counterclockwise_Btn.setEnabled(true);
                }
                return false;
            }
        });

        mStart_DC_Btn = (Button)findViewById(R.id.Start_DCMotor);
        mStart_DC_Btn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDCMotor = 1;

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mDCMotor = 0;
                }
                return false;
            }
        });


        mHandler.postDelayed(SendCommandTimer, 300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Runnable SendCommandTimer = new Runnable() {
        public void run()
        {
            SendCommand();
            mHandler.postDelayed(SendCommandTimer, 300);
        }
    };

    private void SendCommand()
    {
        if (mBlue.SendMessage(mCmd + "#" + mFRightSpeed + "#" + mBRightSpeed + "#" + mFLeftSpeed + "#" + mBLeftSpeed + "#" + mServoAngle + "#" + mDCMotor + "\n") == true) {
            //mMessagebox.setText(mCmd + "#" + mFRightSpeed + "#" + mBRightSpeed + "#" + mFLeftSpeed + "#" + mBLeftSpeed);
        }
        else
        {
            mMessagebox.setText("Bluetooth : Send Command Error");
        }
        Log.d("Albert", "Command : " + mCmd + "#" + mFRightSpeed + "#" + mBRightSpeed + "#" + mFLeftSpeed + "#" + mBLeftSpeed + "#" + mServoAngle + "#" + mDCMotor);

    }
}

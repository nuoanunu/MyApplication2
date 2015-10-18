package com.example.nhat.myapplication;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nhat.myapplication.Tasks.MiddleMan;

import java.util.ArrayList;

public class HeadService extends Service {
    Context mContext;
    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.ic_launcher);

        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //this code is for dragging the chat head

        chatHead.setOnClickListener(mOnClickListener);
        windowManager.addView(chatHead, params);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                Toast.makeText(mContext, "AAAAAAAAA",
                        Toast.LENGTH_LONG).show();
                Intent startMain = new Intent(mContext, MainActivity.class);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                onDestroy();

        }
    };

}
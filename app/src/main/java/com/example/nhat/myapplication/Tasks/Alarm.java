package com.example.nhat.myapplication.Tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.nhat.myapplication.MainActivity;

import java.util.Calendar;

/**
 * Created by Nhat on 9/27/2015.
 */
public class Alarm extends AppCompatActivity {
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String hour = intent.getStringExtra("HOUR");
        final String minute = intent.getStringExtra("MINUTE");
        int a = Integer.parseInt(hour);
        int b = Integer.parseInt(minute);
        createAlarm(a,b);
    }

    public void createAlarm(int a, int b) {

        Intent i = new Intent( AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_HOUR, a);
        i.putExtra(AlarmClock.EXTRA_MINUTES, b);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

        startActivity(i);
        finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

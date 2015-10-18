package com.example.nhat.myapplication.Tasks;

import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nhat.myapplication.MainActivity;
import com.example.nhat.myapplication.R;

public class SetAlarm extends AppCompatActivity {
    int num1;int num2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int hour = Integer.parseInt(intent.getStringExtra("HOUR").trim());
        int minute = Integer.parseInt(intent.getStringExtra("MINUTE").trim());
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);

        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute);

        startActivity(i);

        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_alarm, menu);
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
    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}

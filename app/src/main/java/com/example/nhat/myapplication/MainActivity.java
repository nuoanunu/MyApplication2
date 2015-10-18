package com.example.nhat.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nhat.*;
import com.example.nhat.myapplication.Tasks.Alarm;
import com.example.nhat.myapplication.Tasks.MiddleMan;
import com.example.nhat.myapplication.Tasks.SetAlarm;

public class MainActivity extends Activity {
    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private static final int ALARM_REQUEST_CODE = 1002;
    private EditText metTextHint;
    private ListView mlvTextMatches;
    private Spinner msTextMatches;
    private Button mbtSpeak;
    private Button mPlayButton;
    private Button mStopButton;
    private boolean mIsBound;
    private waiter mBoundService;

    boolean Onservice = false;

    private TextView TV;
    @Override
    public void onBackPressed() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TV = (TextView) findViewById(R.id.RESULT);
        metTextHint = (EditText) findViewById(R.id.etTextHint);
        mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);
        msTextMatches = (Spinner) findViewById(R.id.sNoOfMatches);
        mbtSpeak = (Button) findViewById(R.id.btSpeak);
        Intent intent = getIntent();
        String result = intent.getStringExtra("RESULT");

        if(result!=null)  TV.setText(result);
        checkVoiceRecognition();
        mPlayButton = (Button) findViewById(R.id.play);
        mStopButton = (Button) findViewById(R.id.stop);

    }

    public void checkVoiceRecognition() {

        Log.v("", "checkVoiceRecognition checkVoiceRecognition");
        // Kiem tra thiet bi cho phep nhan dang giong noi hay ko
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            mbtSpeak.setEnabled(false);
            Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();
        }
    }

    // Gui tap tin am thanh
    public  void InstalledApps(View view)
    {
        List<PackageInfo> PackList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < PackList.size(); i++) {
            PackageInfo PackInfo = PackList.get(i);
            if ((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String l = PackInfo.applicationInfo.packageName;
                String AppName = PackInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("App № " + Integer.toString(i), l);
            }
            if((PackInfo.applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                String l = PackInfo.applicationInfo.packageName;
                String AppName = PackInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("App № " + Integer.toString(i), l);
            }
        }
    }

    public void startapp(View view){
        Context ctx=this;
        try {
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage("com.facebook.orca");
            ctx.startActivity(i);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }

    }
    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // xac nhan ung dung muon gui yeu cau
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        // goi y nhung dieu nguoi dung muon noi
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText().toString());

        // goi y nhan dang nhung gi nguoi dung se noi
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        // Kiem tra item muon hien thi da chon tron spinner
        if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Please select No. of Matches from spinner", Toast.LENGTH_SHORT).show();
            return;
        }

        int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem().toString());

        // Xac dinh ban muon bao nhieu ket qua gan dung duoc tra ve
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);

        // Gui yeu cau di
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    // Su kien nhan lai ket qua
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            // Truong hop co gia tri tra ve
            if(resultCode == RESULT_OK) {
                ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                HandleText handler = new HandleText();
                Intent i = new Intent(this, MiddleMan.class);
                i.putExtra("theText", textMatchList.get(0));
                startActivity(i);
                finish();

            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void startService(View view) {
        startService(new Intent(getBaseContext(), HeadService.class));
        finish();
    }

    //method destroy service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), waiter.class));
    }


}
package com.example.nhat.myapplication;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.media.MediaPlayer;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.FsgModel;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static android.widget.Toast.makeText;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class waiter extends Service implements RecognitionListener {
    private boolean started = false;
    private SpeechRecognizer recognizer;
    private static final String KWS_SEARCH = "wakeup";
    private static final String FORECAST_SEARCH = "forecast";
    private static final String DIGITS_SEARCH = "digits";
    private static final String PHONE_SEARCH = "phones";
    private static final String MENU_SEARCH = "menu";
    Context mContext;
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "please call me";

    //Nạp chồng phương thức onBind bằng cách trả lại giá trị mBinder

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Tạo một đối tượng MediaPlayer chơi nhạc đơn giản ( sử dụng để chơi file abc.mp3 đặt trong folder res/raw )
    MediaPlayer mMediaPlayer;

    public void startMp3Player() {
        if(!started) {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(),
                    R.raw.anc);
            mMediaPlayer.start();
            started = true;
        }
    }

    public void mp3Stop() {
        mMediaPlayer.stop();
        started= false;
        mMediaPlayer.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return 0;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        HeavyTask haha = new HeavyTask();
        haha.execute();

    }
    private class HeavyTask extends AsyncTask<Void, Void, Exception>{
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(waiter.this);

                File assetDir = assets.syncAssets();

                setupRecognizer(assetDir);

            } catch (IOException e) {
                e.printStackTrace();
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {


            if(recognizer != null) {switchSearch(KWS_SEARCH);}

        }

    }

    @Override
    public void onDestroy() {
        mp3Stop();
        recognizer.cancel();
        recognizer.shutdown();
        super.onDestroy();
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                        // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(assetsDir)

                        // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)

                        // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create grammar-based search for digit recognition
        File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);

        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);

        // Phonetic search
        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
    }
    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);

    }

    private void switchSearch(String searchName) {
        recognizer.stop();
        Toast.makeText(this, "Listening", Toast.LENGTH_SHORT).show();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);


    }
    @Override
    public void onError(Exception error) {
    }


    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }
    @Override
    public void onResult(Hypothesis hypothesis) {

    }

    @Override
    public void onBeginningOfSpeech() {
    }
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        Toast.makeText(this,text , Toast.LENGTH_SHORT).show();
        if (text.equals(KEYPHRASE)) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock");
            kl.disableKeyguard();

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
            wakeLock.acquire();
            startMp3Player();
        }
        else switchSearch(KWS_SEARCH);


    }

}



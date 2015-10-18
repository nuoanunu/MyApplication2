package com.example.nhat.myapplication.Tasks;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nhat.myapplication.DataObject;
import com.example.nhat.myapplication.GmailContract;
import com.example.nhat.myapplication.MainActivity;
import com.example.nhat.myapplication.R;
import com.example.nhat.myapplication.TaskUtil;


import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MiddleMan extends AppCompatActivity {
    List<DataObject> dataAIML;
    String result1;
    private String host = "http://118.69.135.23/fti-qa/nlp/vi/tagger?text=";
    private ArrayList<String> brightess = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String message = intent.getStringExtra("theText");
        Context context = getApplicationContext();
        CharSequence demo = message;
        int duration = Toast.LENGTH_LONG;
        getData();

        xuLyMessage(message);

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                    getAnswer(host, message);
            }
        }).start();

        //handleWithKey(message);
        */
}

    private  void loadBrightData(){
        brightess.add("độ sáng");
        brightess.add("tăng sáng");
        brightess.add("giảm sáng");
        brightess.add("chỉnh sáng");
        brightess.add("bật sáng");
    }
    public void handleWithKey(String text){

            boolean brightCommand= false;
        for (String str: brightess){
            if (text.contains(str)) {
                brightCommand=true;
                break;
            }
        }

            if(text.contains("báo thức")||text.contains("hẹn giờ")) {
                int k=text.indexOf("giờ");
                String hour = text.substring(k-3, k).trim();
                String minute = text.substring(k+4,k+6).trim();
                Intent setAlarmIntent = new Intent(this, SetAlarm.class);
                setAlarmIntent.putExtra("HOUR",hour);
                setAlarmIntent.putExtra("MINUTE", minute);
                startActivity(setAlarmIntent);
                finish();
            }

                if (brightCommand){
                    String str;
                    str = text.replaceAll("[^0-9]+"," ");
                    str=str.trim();
                    Intent setBright= new Intent(this, SetBrightess.class);
                    setBright.putExtra("LEVEL",str);
                    Log.d("tag", "fdafdas");
                    startActivity(setBright);
                    finish();
                }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_middle_man, menu);
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
        Intent startMain = new Intent(this, MainActivity.class);
        startMain.putExtra("RESULT","Alarm Set OK!");
        startActivity(startMain);
        super.onDestroy();
    }
    public void getAnswer(String host, String question) {
        String s = host+question;
        if(s == null) return;

        String k;
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET,
                s, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result1 = new String();
                        result1= response.toString();
                        Toast.makeText(MiddleMan.this, result1,
                                Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        SmacApplication.getInstance().addToRequestQueue(jsonObjRequest, "jsonobject_request");
    }
    public void getData(){

        try {
            TaskUtil parser = new TaskUtil();
            dataAIML = parser.parse(getAssets().open("AIML.xml"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String xuLyMessage(String message){

        String action="";
        int ActionID=-1;
        boolean flag = true;
        int flag2=-1;
        for(int i=0; i< dataAIML.size() ; i++){
            String[] temp= dataAIML.get(i).getThePartern().split(" ");
            for(int j =0;j<temp.length; j++){
                if(temp[j].charAt(0)!='$'){

                    if(message.contains(temp[j])) {
                        if( message.indexOf(temp[j]) < flag2 ) flag= false;
                        else flag2 = message.indexOf(temp[j]);
                    }
                    else flag = false;
                }
            }
            if(flag) {
                action= dataAIML.get(i).getTheAction();
                ActionID=i;
            }
            else{flag=true;flag2=-1;}
        }
        Toast.makeText(getApplicationContext(),action, Toast.LENGTH_LONG).show();
        if(action.equals("text")){
            String pattern =dataAIML.get(ActionID).getThePartern();
            String pattern1="";
            String pattern2="";
            if( pattern.indexOf("${target}")+10 > pattern.indexOf("${message}") ){
                pattern1 = pattern.substring(0,pattern.indexOf("${target}")).trim();
                String[] temp0 = pattern1.split(" ");
                int k= temp0.length-1;
                message=message.replace(pattern1,"@");
                Message mess = new Message();
                String[] temp = message.split(" ");
                String phonenumber =mess.getPhoneNumber(vietHoa(temp[k]+" "+temp[k+1]+" "+temp[k+2]+" "+temp[k+3]), this);
                if(phonenumber.equals("Unsaved")){ phonenumber =mess.getPhoneNumber( vietHoa(temp[k]+" "+temp[k+1]+" "+temp[k+2]) ,this);}
                if(phonenumber.equals("Unsaved")){ phonenumber =mess.getPhoneNumber( vietHoa(temp[k]+" "+temp[k+1]) ,this);}
                if(phonenumber.equals("Unsaved")){ phonenumber =mess.getPhoneNumber( vietHoa(temp[k]) ,this);}

            }
            else{
                pattern1 = pattern.substring(0,pattern.indexOf("${target}")).trim();
                pattern2=  pattern.substring(pattern.indexOf("${target}")+9,pattern.indexOf("${message}")).trim();
                message=message.replace(pattern1,"@");
                message=message.replace(pattern2, "@");
            }

            String[] temp=message.split("@");


            return "text:";
        }
        else if(action.equals("call")){

            String pattern =dataAIML.get(ActionID).getThePartern();
            message=message.substring(pattern.indexOf("${target}")).trim();

            Message mess = new Message();
            String phonenumber = mess.getPhoneNumber(vietHoa(message),this);
            if(!phonenumber.equals("Unsaved")) {
                try {
                    Intent my_callIntent = new Intent(Intent.ACTION_CALL);
                    my_callIntent.setData(Uri.parse("tel:" + phonenumber));
                    //here the word 'tel' is important for making a call...
                    startActivity(my_callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (action.equals("check mail")){
             result1 = getUnreadMail();
        }
        else if(action.equals("alarm")){
            String[] temp = message.split(" ");
            int hour=-1; int minute=-1;
            for(int i=0; i< temp.length; i ++){
                try {
                    if(hour==-1){
                        int temp2 = Integer.parseInt(temp[i]);
                        if(temp2 > 0) hour = temp2;
                    }
                    else if(minute==-1)
                    {   int temp2 =Integer.parseInt(temp[i]);
                        if(temp2 > 0) minute = temp2;}
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(hour>0) {
                try {
                    Intent my_AlarmIntent = new Intent(this, SetAlarm.class);

                    my_AlarmIntent.putExtra("HOUR", hour+" ");
                    if (minute != -1) my_AlarmIntent.putExtra("MINUTE", minute+" ");
                    else my_AlarmIntent.putExtra("MINUTE", 0);
                    startActivity(my_AlarmIntent);
                } catch (ActivityNotFoundException e) {
                }
            }
        }
        else if(action.equals("openApp")){
            String pattern =dataAIML.get(ActionID).getThePartern();
            message=message.substring(pattern.indexOf("${appname}")).trim();
            if(message.contains("web")){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
            else if(message.contains("camera")|| message.contains("máy ảnh")){
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }

            else {

                List<PackageInfo> PackList = getPackageManager().getInstalledPackages(0);
                for (int i = 0; i < PackList.size(); i++) {
                    PackageInfo PackInfo = PackList.get(i);
                    if ((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        String l = PackInfo.applicationInfo.packageName;
                        if (l.contains(message)) {
                            Context ctx = this;
                            try {
                                Intent openAppp = ctx.getPackageManager().getLaunchIntentForPackage(l);
                                ctx.startActivity(openAppp);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                        }
                    }
                    else if((PackInfo.applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                        String l = PackInfo.applicationInfo.packageName;
                        if (l.contains(message)) {
                            Context ctx = this;
                            try {
                                Intent openAppp = ctx.getPackageManager().getLaunchIntentForPackage(l);
                                ctx.startActivity(openAppp);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                        }
                    }
                }
            }

        }
        return null;
    }
    public String vietHoa(String k){
        String result="";
        String[] temp = k.split(" ");
        for(int i=0;i<temp.length;i++){
            temp[i]= temp[i].substring(0,1).toUpperCase()+temp[i].substring(1);
            result = result +" "+ temp[i];
        }
        return result.trim();

    }
    public void setUp(){
        final String ACCOUNT_TYPE_GOOGLE = "com.google";
        final String[] FEATURES_MAIL = {
                "service_mail"
        };
        AccountManager.get(this).getAccountsByTypeAndFeatures(ACCOUNT_TYPE_GOOGLE, FEATURES_MAIL,
                new AccountManagerCallback<Account[]>() {
                    @Override
                    public void run(AccountManagerFuture future) {
                        Account[] accounts = null;
                        try {
                            accounts = (Account[]) future.getResult();
                            if (accounts != null && accounts.length > 0) {
                                String selectedAccount = accounts[0].name;

                            }

                        } catch (OperationCanceledException oce) {
                            // TODO: handle exception
                        } catch (IOException ioe) {
                            // TODO: handle exception
                        } catch (AuthenticatorException ae) {
                            // TODO: handle exception
                        }
                    }
                }, null /* handler */);
    }
    private String getUnreadMail() {
        String result;
        final String ACCOUNT_TYPE_GOOGLE = "com.google";
        final String[] FEATURES_MAIL = {
                "service_mail"
        };
        AccountManager.get(this).getAccountsByTypeAndFeatures(ACCOUNT_TYPE_GOOGLE, FEATURES_MAIL,
                new AccountManagerCallback<Account[]>() {
                    @Override
                    public void run(AccountManagerFuture future) {
                        Account[] accounts = null;
                        try {
                            accounts = (Account[]) future.getResult();
                            if (accounts != null && accounts.length > 0) {
                                String selectedAccount = accounts[0].name;
                                int count = 0;
                                Cursor c =
                                        getContentResolver().query(GmailContract.Labels.getLabelsUri(selectedAccount),
                                                null, null, null, null);
                                if (c != null) {

                                    if (c.moveToFirst()) {
                                        int unreadColumn = c.getColumnIndex(LabelColumns.NUM_UNREAD_CONVERSATIONS);
                                        int nameColumn = c.getColumnIndex(LabelColumns.NAME);

                                        String name = c.getString(nameColumn);
                                        String unread = c.getString(unreadColumn);//here's the value you need
                                        result1 = new String();
                                        result1 = " bạn có ,"+unread+ "email chưa đọc";

                                    }

                                }
                            }

                        } catch (OperationCanceledException oce) {
                            // TODO: handle exception
                        } catch (IOException ioe) {
                            // TODO: handle exception
                        } catch (AuthenticatorException ae) {
                            // TODO: handle exception
                        }
                    }
                }, null /* handler */);
        return result1;
    }
    public static final class LabelColumns {
        public static final String CANONICAL_NAME = "canonicalName";
        public static final String NAME = "name";
        public static final String NUM_CONVERSATIONS = "numConversations";
        public static final String NUM_UNREAD_CONVERSATIONS = "numUnreadConversations";
    }

}

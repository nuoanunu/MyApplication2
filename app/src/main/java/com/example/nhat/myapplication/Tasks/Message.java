package com.example.nhat.myapplication.Tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import java.util.HashMap;

/**
 * Created by Nhat on 9/27/2015.
 */
public class Message {
    public void sendLongSMS(String sms,String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,sms,null,null);
    }
    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if(ret==null)
            ret = "Unsaved";
        return ret;
    }
    
}

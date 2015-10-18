package com.example.nhat.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import com.example.nhat.myapplication.Tasks.Alarm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Nhat on 9/24/2015.
 */
public class HandleText {

     ArrayList<String> recentTime = new ArrayList<String>(Arrays.asList(
            "hôm nay", "bây giờ", "bữa nay", "lát nữa", "chút nữa", "xíu nữa",
            "lúc này"));
    ArrayList<String> tomorrow = new ArrayList<String>(Arrays.asList(
            "ngày mai", "mai"));
    ArrayList<String> questionKey = new ArrayList<String>(Arrays.asList(
            "ngày mai", "mai"));
     public ArrayList<String> Handle(String voice) {
        String[] chuoi = voice.split(" ");
        String tenphim = "";
        int i = -1;
        ;
        int j = -1;
        ;
        for (int k = 0; k < chuoi.length; k++) {
            if (chuoi[k].equals("phim")) i = k;
            if (chuoi[k].equals("không")) j = k;
        }
        if(j ==-1) { j = chuoi.length-1 ;}
        if (i > -1 && j > -1) {
            for (int k = i + 1; k < j; k++) {
                tenphim = tenphim + " " + chuoi[k];

            }

        }
        tenphim = tenphim.trim();

        String day = "";
        for (int k = 0; k < recentTime.size(); k++) {
            if (voice.contains(recentTime.get(k))) {
                Date toda = new Date();
                //day = (toda.getYear()+1900)+ "-" + (toda.getMonth()+1) + "-" + toda.getDay();
                day="2015-09-25";
            }

        }
         for (int k = 0; k < tomorrow.size(); k++) {
             if (voice.contains(tomorrow.get(k))) {
                 Date toda = new Date();
                 //day = (toda.getYear()+1900)+ "-" + (toda.getMonth()+1) + "-" + toda.getDay();
                 day="2015-09-26";
             }

         }
        //return getData("http://phimchieurap.vn/lich-chieu/?sphim=" + phimCode + "&srap=0&sdate=" + day + "&slocation=10");
        ArrayList<String> result = new ArrayList<>();
         result.add("Ban vua tim lich chieu phim :");
         result.add(tenphim);
         result.add("vao ngay : "+ day);
         return result;
    }

    public  ArrayList<String> getData(String link) {
        ArrayList<String> lich = new ArrayList<>();
        lich.add(link);
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            BufferedReader br = new BufferedReader((new InputStreamReader(in)));
            String temp;
            while ((temp = br.readLine()) != null) {
                temp = temp.trim();
                String temp2="";
                if (temp.indexOf("\"_blank\"> ") == 0 && temp.indexOf("</a>") == (temp.length() - 4)) {

                    temp2 =temp.substring(9, temp.length() - 4)+" ";

                }
                if (temp.indexOf("<div class=\"text\">") == 0 && temp.indexOf("</div>") == (temp.length() - 6)) {
                    temp2= temp2 + temp.substring(18, temp.length() - 6);
                }
                lich.add(temp2);

            }

        } catch (Exception e) {
        }
    return lich;
    }


}



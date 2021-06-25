package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class Helper {

    public static Date addDays(int days, Date date) {
        Long milliseconds = Long.valueOf(days*24*60*60*1000);
        return new Date(date.getTime()+milliseconds);
    }

    public static Date addHours(String time, Date date) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3,5));
        int second = Integer.parseInt(time.substring(6,8));
        Long milliseconds = Long.valueOf((hours*60*60+minute*60+second)*1000);
        return new Date(date.getTime()+milliseconds);
    }

    public static String changeFormat(String date) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);

        return formatDate;
    }

    public static int countAttend(String str) {
        int count = 0;
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) == '1'){
                count++;
            }
        }
        return count;
    }
}

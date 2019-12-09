package com.example.qrfoodproject.FoodDairy.calen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.CalendarView;
import android.widget.Toast;

import com.example.qrfoodproject.R;

public class FoodDairy_Calen extends AppCompatActivity {
    CalendarView calendarview;
    //public static String date;
    public static String date_format;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddairy_calen);

        calendarview = (CalendarView)findViewById(R.id.calendarView);

        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String strmonth;
                String strdate;
                //date = (Integer.toString(year)) +" 年 "+  (Integer.toString(month+1))  +" 月 "+  (Integer.toString(dayOfMonth)) +" 日";
                if(month<10){
                    strmonth = "0"+(month+1);
                }
                else{
                    strmonth = ""+ (month+1);
                }
                if(dayOfMonth<10){
                    strdate = "0"+dayOfMonth;
                }else{
                    strdate = ""+dayOfMonth;
                }
                date_format =year+"-"+strmonth+"-"+strdate;
                Toast.makeText(FoodDairy_Calen.this,date_format,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FoodDairy_Calen.this,FoodDairy_Calen_date.class);
                startActivity(intent);
                finish();
            }
        });

    }
}


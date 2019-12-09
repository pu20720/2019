package com.example.qrfoodproject.FoodDairy;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qrfoodproject.FoodDairy.calen.FoodDairy_Calen;
import com.example.qrfoodproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodDairy_main extends AppCompatActivity {
    private TextView today;
    private Button intake_nutrition;
    private ImageButton calen;
    private TabLayout tablayout;
    private ViewPager viewpager;
    public static FoodDairy_main instance = null; //用來取得當前頁面
    private String[] IconName = {"早餐", "中餐", "晚餐"};

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.fooddairy_main);

        instance = this;//用來取得當前頁面

        setView(); //設定元件ID

        setTitle(); //設定Title為當日日期

        setTab_and_ViewPager(); //設定Tablayout 與 ViewPager 連動

        setCalenButtonListener(); //監聽日曆按鈕

        setIntake_nutritionListener();//監聽攝取營養按鈕

    }

    //給FoodDairy_AddFood調用，用途:關閉此頁面
    @Override
    public void finish() {
        super.finish();
        instance = null;
    }
    private void setTitle(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        today.setText(strDate);
    }
    private void setView() {
        today = (TextView)findViewById(R.id.today);
        calen = (ImageButton) findViewById(R.id.calen);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        intake_nutrition = (Button) findViewById(R.id.intake_nutrition);
    }

    private void setTab_and_ViewPager() {
        for (int i = 0; i < 3; i++)
            tablayout.addTab(tablayout.newTab());
        setViewPager();
        tablayout.setupWithViewPager(viewpager);
        setTabIcon();
    }

    private void setViewPager() {
        FoodDairy_Fragment_breakfast myFragment1 = new FoodDairy_Fragment_breakfast();
        FoodDairy_Fragment_lunch myFragment2 = new FoodDairy_Fragment_lunch();
        FoodDairy_Fragment_dinner myFragment3 = new FoodDairy_Fragment_dinner();
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(myFragment1);
        fragmentList.add(myFragment2);
        fragmentList.add(myFragment3);
        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(myFragmentAdapter);
    }

    private void setTabIcon() {
        for (int i = 0; i < 3; i++) {
            tablayout.getTabAt(i).setText(IconName[i]);
        }
    }
    private void setIntake_nutritionListener(){
        intake_nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(FoodDairy_main.this,FoodDairy_intake_nutrition.class));
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String strDate = sdFormat.format(date);
                Intent intent = new Intent(FoodDairy_main.this,FoodDairy_intake_nutrition.class);
                intent.putExtra("strDate",strDate);
                startActivity(intent);
            }
        });
    }

    private void setCalenButtonListener(){

        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDairy_main.this, FoodDairy_Calen.class);
                startActivity(intent);
            }
        });
    }

}

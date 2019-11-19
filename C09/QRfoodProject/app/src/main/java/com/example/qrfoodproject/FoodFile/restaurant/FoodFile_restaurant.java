package com.example.qrfoodproject.FoodFile.restaurant;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrfoodproject.R;

public class FoodFile_restaurant extends AppCompatActivity {
    private TabLayout tablayout;
    private ViewPager viewpager;
    private String IconName[] = {"靜園","宜園","至善"};
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.foodfile_restaurant);

        setView();
        setTab_and_ViewPager(); //設定Tablayout 與 ViewPager 連動
    }
    private void setView(){
        tablayout = (TabLayout) findViewById(R.id.foodfile_tablayout);
        viewpager = (ViewPager) findViewById(R.id.foodfile_viewpager);

    }
    private void setTab_and_ViewPager() {
        for (int i = 0; i < 3; i++)
            tablayout.addTab(tablayout.newTab());
        setViewPager();
        tablayout.setupWithViewPager(viewpager);
        setTabIcon();
    }
    private void setViewPager() {
        ViewPagerAdapter myFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(myFragmentAdapter);
    }

    private void setTabIcon() {
        for (int i = 0; i < 3; i++) {
            tablayout.getTabAt(i).setText(IconName[i]);
        }
    }
}

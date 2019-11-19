package com.example.qrfoodproject.FoodDairy;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class chartModel {
    private PieChart chart;
    private float user,total,percent;
    DecimalFormat df;
    chartModel(PieChart pieChart,String user,String total){
        chart = pieChart;
        this.user = Float.parseFloat(user);
        this.total = Float.parseFloat(total);
    }
    public void makeChart() {
        df = new DecimalFormat("#.#");
        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(user,"a"));
        if(total-user < 0)
            strings.add(new PieEntry(0));
        else
            strings.add(new PieEntry(total - user));

        PieDataSet dataSet = new PieDataSet(strings, "Label");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(255, 253, 108));
        colors.add(Color.rgb(107, 142, 35));
        dataSet.setColors(colors);


        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);

        pieData.setValueTextSize(20f); //取消數據上的文字
        chart.setData(pieData);
        Description description = new Description();
        description.setText("");
        description.setEnabled(false);
        chart.setDescription(description);
        chart.setDrawEntryLabels(false);

        chart.setTouchEnabled(false); //圖表禁止滑動
        chart.getLegend().setEnabled(false);//禁用圖標圖例（默認是有圖表圖例的）
        chart.setEntryLabelColor(Color.WHITE); //設置條目標籤的顏色
        chart.setEntryLabelTextSize(8f); //設置條目標籤的字體大小
        percent =(user/total*100);
        chart.setCenterText( df.format(percent)+"%"); //設置繪製在餅圖中心的文字內容

        chart.setCenterTextColor(Color.rgb(0, 0, 0)); //設置繪製在餅圖中心的文字顏色
        if(percent > 100) {
            chart.setCenterTextColor(android.graphics.Color.RED);
        }
        chart.setCenterTextSize(13f); //設置繪製在餅圖中心的文字大小
        chart.invalidate();
    }
}

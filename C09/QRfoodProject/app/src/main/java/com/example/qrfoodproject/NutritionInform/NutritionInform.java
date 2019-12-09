package com.example.qrfoodproject.NutritionInform;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qrfoodproject.R;

public class NutritionInform extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutritioninform_main);

        // 營養抓寶站點進『01五穀根莖類』
        Button nutritionInform01 = this.findViewById(R.id.nutritioninform01);
        nutritionInform01.setOnClickListener(nutritionInform01Listener);
        // 營養抓寶站點進『02蔬菜類』
        Button nutritionInform02 = this.findViewById(R.id.nutritioninform02);
        nutritionInform02.setOnClickListener(nutritionInform02Listener);
        // 營養抓寶站點進『03水果類』
        Button nutritionInform03 = this.findViewById(R.id.nutritioninform03);
        nutritionInform03.setOnClickListener(nutritionInform03Listener);
        // 營養抓寶站點進『04蛋豆魚肉類』
        Button nutritionInform04 = this.findViewById(R.id.nutritioninform04);
        nutritionInform04.setOnClickListener(nutritionInform04Listener);
        // 營養抓寶站點進『05低脂乳品類』
        Button nutritionInform05 = this.findViewById(R.id.nutritioninform05);
        nutritionInform05.setOnClickListener(nutritionInform05Listener);
        // 營養抓寶站點進『06油脂與堅果種子類』
        Button nutritionInform06 = this.findViewById(R.id.nutritioninform06);
        nutritionInform06.setOnClickListener(nutritionInform06Listener);
    }
    // 營養抓寶站點進『01五穀根莖類』
    private Button.OnClickListener nutritionInform01Listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NutritionInform.this, NutritionInform_01.class);
            startActivity(intent);
        }
    };
    // 營養抓寶站點進『02蔬菜類』
    private Button.OnClickListener nutritionInform02Listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NutritionInform.this, NutritionInform_02.class);
            startActivity(intent);
        }
    };
    // 營養抓寶站點進『03水果類』
    private Button.OnClickListener nutritionInform03Listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NutritionInform.this, NutritionInform_03.class);
            startActivity(intent);
        }
    };
    // 營養抓寶站點進『04蛋豆魚肉類』
    private Button.OnClickListener nutritionInform04Listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NutritionInform.this, NutritionInform_04.class);
            startActivity(intent);
        }
    };
    // 營養抓寶站點進『05低脂乳品類』
    private Button.OnClickListener nutritionInform05Listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NutritionInform.this, NutritionInform_05.class);
            startActivity(intent);
        }
    };
    // 營養抓寶站點進『06油脂與堅果種子類』
    private Button.OnClickListener nutritionInform06Listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NutritionInform.this, NutritionInform_06.class);
            startActivity(intent);
        }
    };
}

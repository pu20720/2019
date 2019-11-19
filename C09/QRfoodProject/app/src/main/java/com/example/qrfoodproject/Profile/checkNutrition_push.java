package com.example.qrfoodproject.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

public class checkNutrition_push {

    private static String protein;
    private static String fat;
    private static String saturatedFat;
    private static String transFat;
    private static String carbohydrate;
    private static String sugar;
    private static String sodium;

    private static SharedPreferences pref;

    public static void readAndWriteInPref(JSONObject data, Context context){

        //從資料庫(存在session)中將資料轉移到SharedPreferences(方便無session使用)
        //暫定在進入Home_QRFood以及從食物日誌新增資料以後呼叫此function

        try{

            protein = data.getString("protein");
            fat = data.getString("fat");
            saturatedFat = data.getString("saturatedFat");
            transFat= data.getString("transFat");
            carbohydrate = data.getString("carbohydrate");
            sugar = data.getString("sugar");
            sodium = data.getString("sodium");

            pref = context.getSharedPreferences("Nutrition", Context.MODE_PRIVATE);
            //依序  蛋白質、脂肪、飽和脂肪、反式脂肪、碳水化合物、糖、鈉
            pref.edit()
                    .putString("protein", protein)
                    .putString("fat", fat)
                    .putString("saturatedFat", saturatedFat)
                    .putString("transFat", transFat)
                    .putString("carbohydrate" ,carbohydrate)
                    .putString("sugar" , sugar)
                    .putString("sodium" , sodium)
                    .apply();

            Log.d("PushNotification", "Getting the needed data to pass to SharedPreferences via JSONObject");


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String checkAndReturnResult(Context context){

        //從SharedPreferences中取出各項數值，供PushNotification在即便session不存在→無法讀取資料庫的情況下，也可以取得所需資料
        //暫定只會用在處理推播通知

        StringBuilder result = new StringBuilder();
        boolean healthy = true;

        pref = context.getSharedPreferences("Nutrition", Context.MODE_PRIVATE);
        String[] array = new String[]
                {pref.getString("protein", "0"), pref.getString("fat", "0"), pref.getString("saturatedFat", "0"),
                pref.getString("transFat", "0"), pref.getString("carbohydrate", "0"),
                pref.getString("sugar", "0"), pref.getString("sodium", "0")};


        if (Float.valueOf(array[0]) > 60.0){
            result.append("蛋白質 ");
            healthy = false;
        }

        if (Float.valueOf(array[1]) > 80.0){
            result.append("脂肪 ");
            healthy = false;
        }

        if (Float.valueOf(array[2]) > 23.0){
            result.append("飽和脂肪 ");
            healthy = false;
        }

        if (Float.valueOf(array[3]) > 0){
            result.append("反式脂肪 ");
            healthy = false;
        }

        if (Float.valueOf(array[4]) > 360.0){
            result.append("碳水化合物 ");
            healthy = false;
        }

        if (Float.valueOf(array[5]) > 60.0){
            result.append("糖分 ");
            healthy = false;
        }

        if (Float.valueOf(array[6]) > 2400.0){
            result.append("鈉 ");
            healthy = false;
        }

        if (!healthy){
            result.append("攝取過多，攝取營養的同時也別忘記要均衡飲食喔！");
            return result.toString();
        }else{
            return "攝取的營養非常均衡！傑出的一手！";
        }


    }
}

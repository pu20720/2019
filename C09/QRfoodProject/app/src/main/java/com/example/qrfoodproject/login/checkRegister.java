package com.example.qrfoodproject.login;

import java.util.regex.Pattern;

public class checkRegister {

    //using regex for double-checking if insert information is legal

    private String regularAccount = "^[a-zA-Z0-9][a-zA-Z0-9_]{3,9}$";
    private String regularPassword = "^[a-zA-Z0-9][a-zA-Z0-9_]{3,9}$";
    private String regularName = "[a-zA-Z_ ]+$";
    private String regularEmail = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    private String regularHeight = "^[1-2]?[0-9][0-9]$";
    private String regularWeight = "^[1-2]?[0-9][0-9]$";
    private String regularExercise = "^[1-4]$";



    private Pattern parAccount = Pattern.compile(regularAccount);
    private Pattern parPassword = Pattern.compile(regularPassword);
    private Pattern parName = Pattern.compile(regularName);
    private Pattern parEmail = Pattern.compile(regularEmail);
    private Pattern parHeight = Pattern.compile(regularHeight);
    private Pattern parWeight = Pattern.compile(regularWeight);
    private Pattern parExercise = Pattern.compile(regularExercise);

    boolean checkAccount(String account){
        //started by one char without num or illegal char, within the length 4 til 10
        return parAccount.matcher(account).matches();
    }

    public boolean checkPassword(String password){
        return parPassword.matcher(password).matches();
    }

    boolean ifPasswordSame(String password, String confirmPassword){
        if (!password.isEmpty() && !confirmPassword.isEmpty()){
            if (password.equals(confirmPassword)){
                return checkPassword(confirmPassword);
                //maybe redundant?
            }else   return false;
        }else   return false;
    }

    public boolean checkName(String name){
        if (!name.isEmpty()){
            if(name.codePoints().anyMatch(
                    codePoint -> Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN)) {
                //HAN required for SDK 24 or later, check for if there is any Chinese char exist

                if (name.length() <= 15){
                    //single Chinese character required 3 var chars
                    return true;
                }else   return false;

            }else{
                //When the input String was in English
                return parName.matcher(name).matches();
            }
        }else   return false;
    }

    public boolean checkMail(String email){
        return parEmail.matcher(email).matches();
    }

    public boolean checkHeight(String height){
        return parHeight.matcher(height).matches();
    }

    public boolean checkWeight(String weight){
        return parWeight.matcher(weight).matches();
    }

    public boolean checkExercise(String exercise){
        return parExercise.matcher(exercise).matches();
    }

}

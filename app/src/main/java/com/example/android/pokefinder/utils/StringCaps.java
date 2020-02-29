package com.example.android.pokefinder.utils;

public class StringCaps {
    public static String capitalizeFirstLetter(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

package com.example.toasterjava;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    public static void simpleToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }
    public static String getString(String name){
        String msz = "Your name is "+name;
        return msz;
    }
}

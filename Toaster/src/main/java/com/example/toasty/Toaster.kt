package com.example.toasty

import android.content.Context
import android.widget.Toast

public class Toaster {

    fun simpleToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun getString(name: String): String? {
        return "Your name is $name"
    }


}
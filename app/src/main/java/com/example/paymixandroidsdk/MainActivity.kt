package com.example.paymixandroidsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.toasty.Toaster

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val str = com.example.toasterjava.Toaster.getString("Roni")
        com.example.toasterjava.Toaster.simpleToast(this,str.toString())


    }
}
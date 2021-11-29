package com.example.idletexter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hasPermissions(this, Manifest.permission.READ_CALL_LOG)
        hasPermissions(this, Manifest.permission.READ_SMS)

        Formatter().getContactLogs(this)
        Formatter().getAllSms(this)
    }
    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

}
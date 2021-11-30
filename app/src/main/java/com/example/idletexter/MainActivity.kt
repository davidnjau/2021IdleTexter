package com.example.idletexter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        checkUserPermission()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkUserPermission() {

        val list = listOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALL_LOG)

        // Initialize a new instance of ManagePermissions class
        val isPermitted = ManagePermissions(this,list,permissionsRequestCode).checkPermissions()
        if (isPermitted){
            Formatter().getContactLogs(this)
            Formatter().getAllSms(this)
        }
    }


}
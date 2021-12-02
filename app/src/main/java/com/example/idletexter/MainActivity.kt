package com.example.idletexter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
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








class MainActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123
    private val list = listOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.READ_CALL_LOG)

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
            val contactList = Formatter().getContactLogs(this)


            volleyPost(contactList)


//            Formatter().getAllSms(this)
        }

    }

    fun volleyPost(contactList: List<String>) {
        val postUrl = "http://192.168.100.2:8085/api/v1/idle-texter/add-contacts"
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, postUrl,
            Response.Listener { response ->
                Log.d("Response --->", response)

            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this,
                    error.message,
                    Toast.LENGTH_LONG
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                //Adding parameters to request
                val courseList = ArrayList<String>(checkedSet)
                val ID: String = prefProfID.getString(Config.PROFID_SHARED_PREF, "0")
                params[Config.PROFID_SHARED_PREF] = ID
                for (i in 0 until contactList.size()) {
                    params["courselist"] = courseList[i]
                }
                //returning parameter
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
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
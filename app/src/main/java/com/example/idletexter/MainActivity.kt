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

//import org.apache.poi.xssf.usermodel.XSSFSheet

//import org.apache.poi.xssf.usermodel.XSSFWorkbook





class MainActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123
    private val list = listOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_CALL_LOG)

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
            val contactList = Formatter().getContactLogs(this)
            val idletexterData = IdletexterData(contactList)

//            createExcel(idletexterData)

            retrofitCallsAuthentication.uploadContacts(this,idletexterData )

//            Formatter().getAllSms(this)
        }

    }

    private fun createExcel(idletexterData: IdletexterData) {


        savePublicly(idletexterData.contactList)

//        val csv =
//            Environment.getExternalStorageDirectory().absolutePath + "/MyCsvFile.csv" // Here csv file name is MyCsvFile.csv

//        val workbook: XSSFWorkbook = XSSFWorkbook()
//
//        //Create a blank sheet
//
//        //Create a blank sheet
//        val sheet = workbook.createSheet("Employee Data")
//
//        //This data needs to be written (Object[])
//
//        //This data needs to be written (Object[])
//        val data: MutableMap<String, Array<Any>> = TreeMap()
//        data["1"] = arrayOf("ID", "NAME", "LASTNAME")
//        data["2"] = arrayOf(1, "Amit", "Shukla")
//        data["3"] = arrayOf(2, "Lokesh", "Gupta")
//        data["4"] = arrayOf(3, "John", "Adwards")
//        data["5"] = arrayOf(4, "Brian", "Schultz")
//
//        //Iterate over data and write to sheet
//
//        //Iterate over data and write to sheet
//        val keyset: Set<String> = data.keys
//        var rownum = 0
//        for (key in keyset) {
//            val row = sheet.createRow(rownum++)
//            val objArr = data[key]!!
//            var cellnum = 0
//            for (obj in objArr) {
//                val cell = row.createCell(cellnum++)
//                if (obj is String) cell.setCellValue(obj as String) else if (obj is Int) cell.setCellValue(
//                    obj as Int
//                )
//            }
//        }
//        try {
//            //Write the workbook in file system
//            val out = FileOutputStream(File("howtodoinjava_demo.xlsx"))
//            workbook.write(out)
//            out.close()
//            println("howtodoinjava_demo.xlsx written successfully on disk.")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }


//
//        var writer: CSVWriter? = null
//        try {
//            writer = CSVWriter(FileWriter(csv))
//            val data: MutableList<Array<String>> = ArrayList()
////            data.add(arrayOf("Country", "Capital"))
////            data.add(arrayOf("India", "New Delhi"))
////            data.add(arrayOf("United States", "Washington D.C"))
////            data.add(arrayOf("Germany", "Berlin"))
//            val idletexterList = idletexterData.contactList
//            for (contacts in idletexterList){
//                data.add(arrayOf(contacts))
//            }
//
//            writer.writeAll(data) // data is adding to csv
//            writer.close()
////            callRead()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }


    }


    fun savePublicly(idletexterData: HashSet<String>) {

        val xmlFile = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/contacts2.txt"
        )

        writeTextData(xmlFile, idletexterData)
    }
    private fun writeTextData(file: File, idletexterData: HashSet<String>) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)

            for (str in idletexterData) {
                fileOutputStream.write((str + System.lineSeparator()).toByteArray())
            }
//            fileOutputStream.write("data".toByteArray())
            Toast.makeText(this, "Done" + file.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
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
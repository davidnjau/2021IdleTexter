package com.example.idletexter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import androidx.annotation.RequiresApi
import android.provider.Telephony

import org.apache.commons.lang3.StringUtils
import java.lang.RuntimeException
import java.lang.StringBuilder


class Formatter {

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getContactLogs(context: Context): HashSet<String>{

        val contactList = HashSet<String>()

        var stringOutput = ""

        val uriCallLogs = Uri.parse("content://call_log/calls")
        val cursorCallLogs = context.contentResolver.query(uriCallLogs, null, null, null)

        if (cursorCallLogs != null) {
            cursorCallLogs.moveToFirst()
            do {
                val stringNumber: String =
                    cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER))
                contactList.add(stringNumber)

            } while (cursorCallLogs.moveToNext())

        }
        return contactList


    }

    fun getAllSms(context: Context): HashSet<String> {

        val mSmsinboxQueryUri = Uri.parse("content://sms/inbox")
        val c = context.contentResolver.query(
            mSmsinboxQueryUri,
            arrayOf("body", "address"),
            null,
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        )
        val contactList = HashSet<String>()


        if (c != null){
            val totalSMS: Int = c.count
            if (c.moveToFirst()) {
                for (i in 0 until totalSMS) {

                    val address = c.getString(1)
                    if (address != null && address == "MPESA"){
                        val body = c.getString(0).toString()
                        val mpesaMessage = StringUtils.substringBetween(body, "from", "on");
                        if (mpesaMessage != null){
                            val reveredString = StringBuilder(mpesaMessage).reverse().toString()
                            if (reveredString.length > 9){
                                val reversedNo = reveredString.substring(0, 10)
                                //Reverse the string back
                                val newReversedString = StringBuilder(reversedNo).reverse().toString()
                                val firstChar = newReversedString[0].toString()
                                if (firstChar == "7"){
                                    contactList.add(newReversedString)
                                }

                            }
                        }


                    }

//                    lstSms.add(c.getString(0))
                    c.moveToNext()
                }
            } else {
                throw RuntimeException("You have no SMS in Inbox")
            }
            c.close()
        }



        return contactList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllContactData(context: Context): HashSet<UserData>{

        val contactListCallLog = getContactLogs(context)
        val contactListSMS = getAllSms(context)

        val idletexterCalls = SourceData.IDLE_TEXTER_CALLS.name
        val idletexterMpesa = SourceData.IDLE_TEXTER_MPESA.name

        val mergedContactList = HashSet<UserData>()
        for (phoneNumber in contactListCallLog){

            val userData = UserData(idletexterCalls, phoneNumber)
            mergedContactList.add(userData)

        }
        for (phoneNumber in contactListSMS){
            val userData = UserData(idletexterMpesa, phoneNumber)
            mergedContactList.add(userData)
        }

        return mergedContactList


    }

}
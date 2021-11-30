package com.example.idletexter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import android.util.Log
import androidx.annotation.RequiresApi
import android.provider.Telephony

import android.content.ContentResolver
import android.telephony.PhoneNumberUtils
import org.apache.commons.lang3.StringUtils
import java.lang.RuntimeException
import java.lang.StringBuilder


class Formatter {

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getContactLogs(context: Context){

        var stringOutput = ""

        val uriCallLogs = Uri.parse("content://call_log/calls")
        val cursorCallLogs = context.contentResolver.query(uriCallLogs, null, null, null)

        if (cursorCallLogs != null) {
            cursorCallLogs.moveToFirst()
            do {
                val stringNumber: String =
                    cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER))

            } while (cursorCallLogs.moveToNext())

        }

    }

    fun getAllSms(context: Context): List<UserData> {
        val lstSms = ArrayList<UserData>()
        val mSmsinboxQueryUri = Uri.parse("content://sms/inbox")
        val c = context.contentResolver.query(
            mSmsinboxQueryUri,
            arrayOf("body", "address"),
            null,
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        )

        if (c != null){
            val totalSMS: Int = c.count
            if (c.moveToFirst()) {
                for (i in 0 until totalSMS) {

                    val address = c.getString(1)
                    if (address != null && address == "MPESA"){
                        val body = c.getString(0).toString()
                        val mpesaMessage = StringUtils.substringBetween(body, "from", "on");
                        if (mpesaMessage != null){
                            val reveredString = StringBuilder(mpesaMessage).reverse()
                            val phoneNumberReversed = reveredString.substring(0, 10)
                            val userNameReversed = reveredString.substring(10, mpesaMessage.length)
                            val userName = StringBuilder(userNameReversed).reverse()
                            val newPhoneNumber = StringBuilder(phoneNumberReversed).reverse()

                            var lastDigit = ""
                            val last1 = takeLast(userName.toString(), 1) //Output: g
                            if (last1 == "0"){
                                lastDigit = userName.substring(0, userName.length -1)
                            }else {

                                val last3 = takeLast(userName.toString(), 3)
                                if(last3.contains("254")){
                                    lastDigit = userName.substring(0, userName.length - 3)
                                }

                            }

                            val userData = UserData(lastDigit, newPhoneNumber.toString())
                            lstSms.add(userData)

//                            Log.e("---$i ", lastDigit.toString())
//                            Log.e("+++$i ", newPhoneNumber.toString())

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



        return lstSms
    }

    private fun takeLast(value: String, count: Int): String{
        if (value.trim { it <= ' ' }.isEmpty()) return ""
        if (count < 1) return ""
        return if (value.length > count) {
            value.substring(value.length - count)
        } else {
            value
        }
    }

    fun formatNumber(number: String): String  {

        var finalReversedNo = ""

        if (number.length >= 9){

            var number2 = number.toLong()

            var num = number2
            var reversed = 0L
            while (num != 0L) {
                val digit = num % 10
                reversed = reversed * 10 + digit
                num /= 10
            }

            var reversedNo = reversed.toString().substring(0, 9).toLong()
            var reversedFinal = 0L
            while (reversedNo != 0L) {
                val digit = reversedNo % 10
                reversedFinal = reversedFinal * 10 + digit
                reversedNo /= 10
            }

            finalReversedNo = "0$reversedFinal"

        }else{

            finalReversedNo = "invalid phone number"

        }



        return finalReversedNo.toString()
    }
}
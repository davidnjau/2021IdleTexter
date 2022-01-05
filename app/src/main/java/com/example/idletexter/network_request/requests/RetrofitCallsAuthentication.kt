package com.example.idletexter.network_request.requests

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.idletexter.*
import com.example.idletexter.network_request.builder.RetrofitBuilder
import com.example.idletexter.network_request.interfaces.Interface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RetrofitCallsAuthentication {

//    var customDialogToast = CustomDialogToast()

    fun uploadContacts(context: Context, idletexterData: IdletexterData){

        CoroutineScope(Dispatchers.Main).launch {

            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {

                uploadContactsData(context, idletexterData)

            }.join()
        }

    }
    private suspend fun uploadContactsData(context: Context, idletexterData: IdletexterData) {

        val job1 = Job()
        CoroutineScope(Dispatchers.Main + job1).launch {

//            val progressDialog = ProgressDialog(context)
//            progressDialog.setTitle("Please wait..")
//            progressDialog.setMessage("Upload in progress..")
//            progressDialog.setCanceledOnTouchOutside(false)
//            progressDialog.show()

            var messageToast = ""
            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {

                val baseUrl = context.getString(UrlData.BASE_URL.message)
                val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
                val apiInterface = apiService.uploadContacts(idletexterData)
                apiInterface.enqueue(object : Callback<SuccessMessage> {
                    override fun onResponse(
                        call: Call<SuccessMessage>,
                        response: Response<SuccessMessage>
                    ) {

//                        CoroutineScope(Dispatchers.Main).launch { progressDialog.dismiss() }

                        if (response.isSuccessful) {

                            var role = ""
                            messageToast = "You have uploaded successfully."

                            val responseBody = response.body()
                            if (responseBody != null){

                                val details = responseBody.details


                            }

//                            CoroutineScope(Dispatchers.Main).launch {
//                                customDialogToast.customDialogToast(
//                                    context as Activity,
//                                    messageToast
//                                )
//
//
//                            }

                        } else {



                            val code = response.code()

                            if (code != 500) {

                                val gson = Gson()
                                val type = object : TypeToken<ErrorMessage>() {}.type
                                val errorDetails : ErrorMessage? = gson.fromJson(response.errorBody()!!.charStream(), type)
                                messageToast = errorDetails?.error ?: "Please try again later."

                                CoroutineScope(Dispatchers.IO).launch {

//                                    CoroutineScope(Dispatchers.Main).launch {
//                                        customDialogToast.customDialogToast(
//                                            context as Activity,
//                                            messageToast
//                                        )
//                                    }



                                }

                            } else {
                                messageToast =
                                    "We are experiencing some server issues. Please try again later"

//                                CoroutineScope(Dispatchers.Main).launch {
//                                    customDialogToast.customDialogToast(
//                                        context as Activity,
//                                        messageToast
//                                    )
//                                }
                            }



                        }


                    }

                    override fun onFailure(call: Call<SuccessMessage>, t: Throwable) {
                        Log.e("-*-*error ", t.localizedMessage)
                        messageToast = "There is something wrong. Please try again"
                        CoroutineScope(Dispatchers.Main).launch {
//                            customDialogToast.customDialogToast(
//                                context as Activity,
//                                messageToast
//                            )
                        }

//                        progressDialog.dismiss()
                    }
                })



            }.join()

        }

    }


}


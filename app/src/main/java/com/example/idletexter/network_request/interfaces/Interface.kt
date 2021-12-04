package com.example.idletexter.network_request.interfaces

import com.example.idletexter.IdletexterData
import com.example.idletexter.SuccessMessage
import retrofit2.Call
import retrofit2.http.*


interface Interface {

    @POST("/api/v1/idle-texter/add-contacts")
    fun uploadContacts(@Body idletexterData: IdletexterData): Call<SuccessMessage>

}
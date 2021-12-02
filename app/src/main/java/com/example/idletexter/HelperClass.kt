package com.example.idletexter

data class UserData(
    val userName: String,
    val phoneNumber: String
)
data class IdletexterData(
    val contactList : List<String>
)
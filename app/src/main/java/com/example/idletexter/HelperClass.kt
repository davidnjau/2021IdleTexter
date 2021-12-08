package com.example.idletexter

data class UserData(
    val userName: String,
    val phoneNumber: String
)
data class IdletexterData(
    val contactList : HashSet<String>
)
data class SuccessMessage(
    val details: String

)
enum class UrlData(var message: Int) {
    BASE_URL(R.string.base_url),
}
data class ErrorMessage(
    val error: String
)
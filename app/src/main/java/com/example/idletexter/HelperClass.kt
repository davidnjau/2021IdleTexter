package com.example.idletexter

data class UserData(
    val contactType: String,
    val phoneNumber: String
)
data class IdletexterData(
    val contactList : HashSet<UserData>
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
enum class SourceData() {
    IDLE_TEXTER_CALLS,
    IDLE_TEXTER_MPESA,

}
package com.example.up_piatnitskii.data.Model

data class Profile(
    val id: String? = null,
    val userId: String? = null,
    val photo: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null
)


fun Profile.toMap(): Map<String, Any?> = mapOf(
    "user_id" to userId,
    "firstname" to firstname,
    "lastname" to lastname,
    "address" to address,
    "phone" to phone
)


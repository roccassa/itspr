package com.itspr.micro.screens

data class UserProfile(
    val userId: String = "",
    val fullName: String = "",
    val age: Int? = null,
    val city: String = "",
    val phoneNumber: String = "",
    val photoBase64: String = ""
 )

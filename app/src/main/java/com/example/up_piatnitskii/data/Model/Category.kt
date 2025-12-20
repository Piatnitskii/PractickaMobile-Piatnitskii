package com.example.up_piatnitskii.data.Model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val name: String,

    val isSelected: Boolean = false
)
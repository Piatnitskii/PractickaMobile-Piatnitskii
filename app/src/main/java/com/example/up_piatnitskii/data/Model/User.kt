package com.example.up_piatnitskii.data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val email: String,
    val password: String
)
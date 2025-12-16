package com.example.up_piatnitskii.data.Model

import android.app.Application
import androidx.room.Room

class StudentApplication: Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }
}

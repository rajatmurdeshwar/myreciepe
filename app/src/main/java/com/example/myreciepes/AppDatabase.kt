package com.example.myreciepes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val getDao:Dao
}
package com.example.myrecipes.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalRecipe::class], exportSchema = false, version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): RecipeDao
}
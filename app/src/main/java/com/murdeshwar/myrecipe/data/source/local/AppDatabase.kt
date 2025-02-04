package com.murdeshwar.myrecipe.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalRecipe::class, LocalIngredient::class, LocalStep::class], exportSchema = false, version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): RecipeDao
}
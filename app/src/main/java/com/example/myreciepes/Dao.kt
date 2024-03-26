package com.example.myreciepes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao  {

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<Recipe>>

}
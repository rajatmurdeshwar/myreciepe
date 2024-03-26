package com.example.myrecipes

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao  {

    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): Flow<List<Recipe>>

}
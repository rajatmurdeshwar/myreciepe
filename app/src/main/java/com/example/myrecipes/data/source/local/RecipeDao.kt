package com.example.myrecipes.data.source.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao  {

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<LocalRecipe>>

}
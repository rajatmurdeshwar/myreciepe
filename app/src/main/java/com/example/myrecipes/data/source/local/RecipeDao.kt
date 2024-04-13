package com.example.myrecipes.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.myrecipes.data.source.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao  {
    @Upsert
    suspend fun insertRecipe(recipe: LocalRecipe)

    @Upsert
    suspend fun insertAll(recipe: List<LocalRecipe>)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<LocalRecipe>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
     fun getById(recipeId: Int): Flow<LocalRecipe?>

}
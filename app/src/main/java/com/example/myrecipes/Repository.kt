package com.example.myrecipes

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface Repository {

    suspend fun getAllRecipes(): Flow<List<Recipe>>
}

class RepositoryImpl @Inject constructor(
    private val dao: Dao,
) : Repository {
    override suspend fun getAllRecipes(): Flow<List<Recipe>> {
        return withContext(IO) {
            dao.getAllRecipes()
        }
    }

}
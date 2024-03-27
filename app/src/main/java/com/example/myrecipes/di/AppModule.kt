package com.example.myrecipes.di

import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.room.Room
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.RepositoryImpl

import com.example.myrecipes.data.source.local.AppDatabase
import com.example.myrecipes.data.source.local.RecipeDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: RepositoryImpl): Repository
}

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideMyDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "MyDataBase"
        )
            .createFromAsset("recipe.db")
            .build()
    }



    @Provides
    fun provideMyRepository(mydb: AppDatabase): RecipeDao = mydb.getDao()
}
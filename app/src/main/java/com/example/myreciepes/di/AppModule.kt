package com.example.myreciepes.di

import android.app.Application
import androidx.room.Room
import com.example.myreciepes.AppDatabase
import com.example.myreciepes.Repository
import com.example.myreciepes.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideMyDataBase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "MyDataBase"
        )
            .createFromAsset("recipe.db")
            .build()
    }



    @Provides
    @Singleton
    fun provideMyRepository(mydb:AppDatabase) : Repository {
        return RepositoryImpl(mydb.getDao)
    }
}
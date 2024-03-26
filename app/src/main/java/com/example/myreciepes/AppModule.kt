package com.example.myreciepes

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideMyDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "MyDataBase"
        )
            .createFromAsset("recipe.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideMyRepository(mydb:AppDatabase) :Repository {
        return RepositoryImpl(mydb.getDao)
    }


}
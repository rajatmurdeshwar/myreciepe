package com.example.myrecipes.di

import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.room.Room
import com.example.myrecipes.BuildConfig
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.RepositoryImpl

import com.example.myrecipes.data.source.local.AppDatabase
import com.example.myrecipes.data.source.local.RecipeDao
import com.example.myrecipes.data.source.network.NetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            .build()
    }


    @Provides
    fun provideMyRepository(mydb: AppDatabase): RecipeDao = mydb.getDao()
}

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }


    @Provides
    @Singleton
    fun provideRecipeApi(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.RECIPE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): NetworkDataSource {
        return retrofit.create(NetworkDataSource::class.java)
    }


}
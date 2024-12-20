package com.murdeshwar.myrecipe.di

import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.room.Room
import com.murdeshwar.myrecipe.BuildConfig
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.RepositoryImpl

import com.murdeshwar.myrecipe.data.source.local.AppDatabase
import com.murdeshwar.myrecipe.data.source.local.RecipeDao
import com.murdeshwar.myrecipe.data.source.network.NetworkDataSource
import com.murdeshwar.myrecipe.data.source.network.NewRecipeApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: RepositoryImpl): Repository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FoodApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalApi

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
    @FoodApi
    fun provideRecipeApi(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.RECIPE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @LocalApi
    fun provideNewRecipeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @FoodApi
    fun provideApi(@FoodApi retrofit: Retrofit): NetworkDataSource {
        return retrofit.create(NetworkDataSource::class.java)
    }

    @Provides
    @Singleton
    @LocalApi
    fun provideLocalApi(@LocalApi retrofit: Retrofit): NewRecipeApiService {
        return retrofit.create(NewRecipeApiService::class.java)
    }


}
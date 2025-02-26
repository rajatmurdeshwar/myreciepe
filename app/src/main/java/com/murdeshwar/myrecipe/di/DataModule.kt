package com.murdeshwar.myrecipe.di

import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.murdeshwar.myrecipe.BuildConfig
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.RepositoryImpl

import com.murdeshwar.myrecipe.data.source.local.AppDatabase
import com.murdeshwar.myrecipe.data.source.local.RecipeDao
import com.murdeshwar.myrecipe.data.source.network.NetworkDataSource
import com.murdeshwar.myrecipe.data.source.network.NewRecipeApiService
import com.murdeshwar.myrecipe.util.AuthInterceptor
import com.murdeshwar.myrecipe.util.ConnectivityManagerNetworkMonitor
import com.murdeshwar.myrecipe.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: RepositoryImpl): Repository
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("user_prefs")
        }
    }
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

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatcherModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(dataStore: DataStore<Preferences>): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(dataStore))
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
            .baseUrl("https://recipe-maker-7b330babacd7.herokuapp.com/")
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

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): NetworkMonitor {
        return ConnectivityManagerNetworkMonitor(context, ioDispatcher)
    }

}
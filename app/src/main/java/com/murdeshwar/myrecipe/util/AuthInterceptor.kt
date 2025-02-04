package com.murdeshwar.myrecipe.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlPath = originalRequest.url().encodedPath() // Use property directly

        // Intercept only the specified paths
        if (urlPath.startsWith("/api/") || urlPath.startsWith("/api/recipes") || urlPath.startsWith("/auth/userDetails")) {
            val token = runBlocking {
                dataStore.data
                    .map { preferences ->
                        preferences[stringPreferencesKey("jwt_token")] // Retrieve token
                    }
                    .firstOrNull()
            }

            // If token is not null, add Authorization header
            val modifiedRequest = token?.let {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $it")
                    .build()
            } ?: originalRequest

            return chain.proceed(modifiedRequest)
        }

        // Proceed with the original request for other paths
        return chain.proceed(originalRequest)
    }
}


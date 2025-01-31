package com.murdeshwar.myrecipe.ui.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIEvent>(UIEvent.Idle)
    val uiState: StateFlow<UIEvent> = _uiState
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    // Signup function
    fun signup(user: User) = viewModelScope.launch {
        try {
            repository.signupUser(user)
            _uiState.value = UIEvent.Success("User registered successfully!")
        } catch (e: Exception) {
            _uiState.value = UIEvent.Error("Signup failed: ${e.message}")
        }
    }

    // Login function
    fun login(user: LoginUser) = viewModelScope.launch {
        try {
            val token = repository.loginUser(user)
            Timber.d("UserViewModel tokenn", "Token :$token")
            if (token != null) {
                Timber.d("UserViewModel", "Token :$token")
                saveToken(token)
            } // Save the token to DataStore
            _uiState.value = UIEvent.Success("Login successful!")
        } catch (e: Exception) {
            _uiState.value = UIEvent.Error("Login failed: ${e.message}")
        }
    }

    private fun saveToken(token: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                Timber.d("UserViewModel saveToken $token")
                preferences[stringPreferencesKey("jwt_token")] = token
            }
        }
    }



    fun saveLoginState(isLoggedIn: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey("isLoggedIn")] = isLoggedIn
            }
        }
    }

    // Read login state from DataStore
    fun readLoginState(): Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[booleanPreferencesKey("isLoggedIn")] ?: false
        }

    // Reset UI state after handling
    fun resetUIState() {
        _uiState.value = UIEvent.Idle
    }

    // UI event sealed class for success/error handling
    sealed class UIEvent {
        object Idle : UIEvent()
        data class Success(val message: String) : UIEvent()
        data class Error(val message: String) : UIEvent()
    }


}
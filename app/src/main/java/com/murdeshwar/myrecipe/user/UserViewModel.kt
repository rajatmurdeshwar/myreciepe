package com.murdeshwar.myrecipe.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIEvent>(UIEvent.Idle)
    val uiState: StateFlow<UIEvent> = _uiState

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
            repository.loginUser(user)
            _uiState.value = UIEvent.Success("Login successful!")
        } catch (e: Exception) {
            _uiState.value = UIEvent.Error("Login failed: ${e.message}")
        }
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
package com.murdeshwar.myrecipe.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails

    init {
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.userDetails()
                _userDetails.value = user
            } catch (e: Exception) {
                // Handle errors
                Timber.tag("ProfileViewModel").e("Error fetching user details: %s", e.message)
            }
        }
    }
}

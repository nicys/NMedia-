package ru.netology.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.auth.AppAuth
import ru.netology.auth.AuthState
import ru.netology.repository.PostRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: AppAuth,
    private val repository: PostRepository,
) : ViewModel() {
    val data: LiveData<AuthState> = auth.authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = auth.authStateFlow.value.id != 0L

    fun authentication(login: String, password: String) {
        viewModelScope.launch {
            repository.authentication(login, password)
        }
    }

    fun registration(nameUser: String, login: String, password: String) {
        viewModelScope.launch {
            repository.registration(nameUser, login, password)
        }
    }
}
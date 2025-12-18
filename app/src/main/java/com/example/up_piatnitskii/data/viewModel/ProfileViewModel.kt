package com.example.up_piatnitskii.data.viewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.compose.runtime.State
import com.example.up_piatnitskii.data.Model.Profile
import com.example.up_piatnitskii.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: Profile) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(private val supabaseClient: SupabaseClient) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState

    fun loadProfile(context: Context) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val profile = supabaseClient.getProfile(context)
                _profileState.value = ProfileState.Success(profile)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun updateProfile(context: Context, profile: Profile) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                supabaseClient.updateProfile(context, profile)
                _profileState.value = ProfileState.Success(profile)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Ошибка сохранения")
            }
        }
    }
}

class SupabaseClient {
    private val userManagementService = RetrofitInstance.userManagementService

    suspend fun getProfile(context: Context): Profile {
        val userId = getUserId(context) ?: throw Exception("Пользователь не авторизован")
        val response = userManagementService.getProfile("eq.$userId")
        if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
            return response.body()!!.first()
        } else {
            throw Exception("Профиль не найден")
        }
    }

    suspend fun updateProfile(context: Context, profile: Profile) {
        val userId = getUserId(context) ?: throw Exception("Пользователь не авторизован")
        val Id = getProfileId(context) ?: throw Exception("Пользователь не авторизован")
        val response = userManagementService.updateProfile(
            "eq.$Id",
            mapOf(
                "user_id" to userId,
                "firstname" to (profile.firstname ?: ""),
                "lastname" to (profile.lastname ?: ""),
                "address" to (profile.address ?: ""),
                "phone" to (profile.phone ?: ""),
                "photo" to (profile.photo ?: "")
            )
        )

        if (!response.isSuccessful) {
            throw Exception(response.message() ?: "Неизвестная ошибка")
        }
    }
}






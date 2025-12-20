package com.example.up_piatnitskii.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.up_piatnitskii.data.Model.ForgotPasswordRequest
import com.example.up_piatnitskii.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException


class ForgotPasswordViewModel : ViewModel() {


    fun forgotPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = ForgotPasswordRequest(email)
                val response = RetrofitInstance.userManagementService
                    .recoverPassword(request)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Ошибка: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Проблема с сетью")
            }
        }
    }
}
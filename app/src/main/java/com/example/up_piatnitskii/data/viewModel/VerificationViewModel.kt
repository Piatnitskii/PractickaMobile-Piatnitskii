package com.example.up_piatnitskii.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VerificationViewModel : ViewModel() {

    fun requestNewCode(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // запрос на сервер для отправки нового кода
                // RetrofitInstance.userManagementService.requestOtp(...)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Ошибка")
            }
        }
    }

    fun verifyCode(
        code: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // отправка кода на сервер для проверки
                // val response = RetrofitInstance.userManagementService.verifyOtp(code)
                val isValid = true // обработай ответ сервера

                if (isValid) onSuccess()
                else onError("Неверный код")
            } catch (e: Exception) {
                onError(e.message ?: "Ошибка")
            }
        }
    }
}
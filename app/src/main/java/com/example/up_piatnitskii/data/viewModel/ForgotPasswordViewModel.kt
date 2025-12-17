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

    // UI State
    private val _passwordRecoveryState = MutableStateFlow<PasswordRecoveryState>(PasswordRecoveryState.Idle)
    val passwordRecoveryState: StateFlow<PasswordRecoveryState> = _passwordRecoveryState

    // Email для валидации
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    // Флаг валидности email
    val isEmailValid = MutableStateFlow(false)

    fun updateEmail(email: String) {
        _email.value = email
        validateEmail(email)
    }

    private fun validateEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        isEmailValid.value = email.matches(emailPattern)
    }

    /**
     * Отправка запроса на восстановление пароля
     */
    fun recoverPassword() {
        // Проверяем валидность email
        if (!isEmailValid.value) {
            _passwordRecoveryState.value = PasswordRecoveryState.Error("Please enter a valid email address")
            return
        }

        viewModelScope.launch {
            _passwordRecoveryState.value = PasswordRecoveryState.Loading

            try {
                Log.d("ForgotPasswordViewModel", "Sending recovery request for email: ${_email.value}")

                // Создаем запрос
                val request = ForgotPasswordRequest(
                    email = _email.value
                )

                // Отправляем запрос через Retrofit
                val response = RetrofitInstance.userManagementService.recoverPassword(request)

                Log.d("ForgotPasswordViewModel", "Response received: ${response.isSuccessful}")

                if (response.isSuccessful) {

                }
            } catch (e: Exception) {
                // Обработка исключений
                val errorMessage = when (e) {
                    is ConnectException -> "No internet connection. Please check your network."
                    is SocketTimeoutException -> "Connection timeout. Please try again."
                    else -> {
                        Log.e("ForgotPasswordViewModel", "Exception: ${e.message}", e)
                        "Recovery failed: ${e.message ?: "Unknown error"}"
                    }
                }
                _passwordRecoveryState.value = PasswordRecoveryState.Error(errorMessage)
            }
        }
    }

    /**
     * Парсинг ошибок восстановления пароля
     */
    private fun parseRecoveryError(errorCode: Int, errorMessage: String, errorBody: String): String {
        return when (errorCode) {
            400 -> {
                when {
                    errorBody.contains("email", ignoreCase = true) -> "Invalid email address"
                    errorBody.contains("rate limit", ignoreCase = true) -> "Too many requests. Please try again later."
                    else -> "Bad request: $errorMessage"
                }
            }
            401 -> "Unauthorized. Please check your API key."
            403 -> "Forbidden. You don't have permission to perform this action."
            404 -> "User with this email not found."
            422 -> "Invalid email format."
            429 -> "Too many attempts. Please try again in a few minutes."
            500 -> "Server error. Please try again later."
            502 -> "Bad gateway. Service temporarily unavailable."
            503 -> "Service unavailable. Please try again later."
            else -> "Recovery failed: $errorMessage"
        }
    }

    /**
     * Парсинг ошибки из тела ответа
     */
    private fun parseRecoveryError(errorBody: String): String {
        return when {
            errorBody.contains("user not found", ignoreCase = true) ->
                "No account found with this email address."
            errorBody.contains("rate limit", ignoreCase = true) ->
                "Too many password reset attempts. Please try again later."
            errorBody.contains("email not confirmed", ignoreCase = true) ->
                "Email not confirmed. Please verify your email first."
            errorBody.contains("disabled", ignoreCase = true) ->
                "Account is disabled. Please contact support."
            else -> "Recovery failed: $errorBody"
        }
    }

    /**
     * Сброс состояния
     */
    fun resetState() {
        _passwordRecoveryState.value = PasswordRecoveryState.Idle
    }
}

/**
 * Состояния восстановления пароля
 */
sealed class PasswordRecoveryState {
    object Idle : PasswordRecoveryState()
    object Loading : PasswordRecoveryState()
    data class Success(val message: String) : PasswordRecoveryState()
    data class Error(val message: String) : PasswordRecoveryState()
}
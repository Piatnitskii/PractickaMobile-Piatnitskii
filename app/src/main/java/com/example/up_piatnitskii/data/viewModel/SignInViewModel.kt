package com.example.up_piatnitskii.data.viewModel

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.up_piatnitskii.MainActivity
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.data.RetrofitInstance
import com.example.up_piatnitskii.data.Model.SignInRequest
import com.example.up_piatnitskii.data.Model.UserDAO
import kotlinx.coroutines.launch

class SignInViewModel (private val userDAO: UserDAO): ViewModel() {
    var email: String = ""
    var password: String = ""

    fun signIn(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val signInData = SignInRequest(email, password)
            val response = RetrofitInstance.userManagementService.signIn(signInData)

            if (response.isSuccessful){
                response.body()?.let {
                    Log.v("SignIn", "Пользователь успешно авторизован: ${it.email}")
                    onSuccess()
                }
            }
            else {
                Log.e("SignIn", "HTTP ошибка: ${response.code()} - ${response.message()}")

                val errorMessage = when
                                           (response.code())
                {
                    400 -> "Неверный email или пароль"
                    422 -> "Некорректные данные"
                    500 -> "Ошибка сервера"
                    else -> "Ошибка входа: ${response.message()}"
                }

                val errorBody = response.errorBody()?.string()
                Log.e("SignIn", "Тело ошибки: $errorBody")
                onError(errorMessage)
            }
        }
    }
}

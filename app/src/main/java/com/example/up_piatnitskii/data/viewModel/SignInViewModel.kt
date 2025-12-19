package com.example.up_piatnitskii.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.up_piatnitskii.data.RetrofitInstance
import com.example.up_piatnitskii.data.Model.SignInRequest
import com.example.up_piatnitskii.data.Model.UserDAO
import kotlinx.coroutines.launch

import android.util.Base64
import com.google.gson.Gson


class SignInViewModel (private val userDAO: UserDAO ): ViewModel() {
    var email: String = ""
    var password: String = ""

    fun signIn(onSuccess: () -> Unit, onError: (String) -> Unit,context: Context) {
        viewModelScope.launch {
            val signInData = SignInRequest(email, password)
            val response = RetrofitInstance.userManagementService.signIn(signInData)

            if (response.isSuccessful){
                response.body()?.let { SignInResponse ->

                    val token = SignInResponse.access_token
                    val payload = decodeJwt(token)
                    val userId = payload.sub

                    saveUserId(context, userId)

                    // Проверяем, есть ли уже профиль у пользователя
                    try {
                        val profileResponse = RetrofitInstance.userManagementService.getProfile("eq.$userId")
                        if (profileResponse.isSuccessful && profileResponse.body()?.isEmpty() == true) {
                            // Профиль не найден, создаём новый
                            val createProfileResponse = RetrofitInstance.userManagementService.createProfile(
                                mapOf(
                                    "user_id" to userId
                                )
                            )

                            if (createProfileResponse.isSuccessful) {
                                val profile = createProfileResponse.body()?.firstOrNull()
                                val profileId = profile?.id
                                saveProfileId(context, profileId)
                                Log.v("SignIn", "ID профиля: $profileId")
                                Log.v("SignIn", "Профиль успешно авторизован: ${SignInResponse.email}")
                            }
                        }else{
                            val profileResponse = RetrofitInstance.userManagementService.getProfile("eq.$userId")
                            val profile = profileResponse.body()?.firstOrNull()
                            val profileId = profile?.id
                            saveProfileId(context, profileId)
                        }


                    } catch (e: Exception) {
                        Log.e("SignIn", "Ошибка при проверке/создании профиля: ${e.message}")
                    }


                    Log.v("SignIn", "Пользователь успешно авторизован: ${SignInResponse.email}")
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


fun getUserId(context: Context): String? {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return prefs.getString("user_id", null)
}


fun saveUserId(context: Context, userId: String) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("user_id", userId).apply()
}

fun getProfileId(context: Context): String? {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return prefs.getString("profile_id", null)
}
fun saveProfileId(context: Context, profileId: String?) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("profile_id", profileId).apply()
}

//@OptIn(ExperimentalEncodingApi::class)
data class JwtPayload(val sub: String)

fun decodeJwt(jwt: String): JwtPayload {
    val parts = jwt.split(".")
    val payload = String(Base64.decode(parts[1], Base64.DEFAULT))
    return Gson().fromJson(payload, JwtPayload::class.java)
}

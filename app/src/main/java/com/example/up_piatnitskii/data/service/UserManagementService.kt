package com.example.up_piatnitskii.data.service

import com.example.up_piatnitskii.data.Model.ForgotPasswordRequest
import com.example.up_piatnitskii.data.Model.ForgotPasswordResponse
import com.example.up_piatnitskii.data.Model.SignInRequest
import com.example.up_piatnitskii.data.Model.SignUpRequest
import com.example.up_piatnitskii.data.Model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFncWVsdWZ6aHZsb29lYXVlaWRrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4NDMyOTAsImV4cCI6MjA4MTQxOTI5MH0.B7lxXCUmLD-3BdEM7w430lxkO1vVWZz2dv4i8P7IP9s"
interface UserManagementService {
    // Sign UP
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpRequest>
    // Sign IN
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInRequest>
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/recover")
    suspend fun recoverPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>

    // 1. Запрос нового кода (OTP)
    @Headers("apikey: $SUPABASE_KEY")
    @POST("your-otp-endpoint/request")              // TODO: замени на свой путь
    suspend fun requestOtp(
        @Body request: OtpRequest                   // TODO: создай свою модель (например, с email)
    ): Response<OtpRequestResponse>                 // или другой тип ответа

    // 2. Проверка кода на сервере
    @Headers("apikey: $SUPABASE_KEY")
    @POST("your-otp-endpoint/verify")               // TODO: замени на свой путь
    suspend fun verifyOtp(
        @Body request: OtpVerifyRequest             // модель с кодом (и email, если нужно)
    ): Response<OtpVerifyResponse>                  // тип ответа сервера
}
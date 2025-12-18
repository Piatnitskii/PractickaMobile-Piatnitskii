package com.example.up_piatnitskii.data.service

import com.example.up_piatnitskii.data.Model.ForgotPasswordRequest
import com.example.up_piatnitskii.data.Model.ForgotPasswordResponse
import com.example.up_piatnitskii.data.Model.Profile
import com.example.up_piatnitskii.data.Model.SignInRequest
import com.example.up_piatnitskii.data.Model.SignInResponse
import com.example.up_piatnitskii.data.Model.SignUpRequest
import com.example.up_piatnitskii.data.Model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFncWVsdWZ6aHZsb29lYXVlaWRrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4NDMyOTAsImV4cCI6MjA4MTQxOTI5MH0.B7lxXCUmLD-3BdEM7w430lxkO1vVWZz2dv4i8P7IP9s"
interface UserManagementService {
    // Sign UP
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpRequest>


    // Sign IN
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    //FORGOT PASSWORD
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/recover")
    suspend fun recoverPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>



    //PROFILES


    @Headers("apikey: $SUPABASE_KEY")
    @POST("rest/v1/profiles")
    suspend fun updateProfile(
        @Query("id", encoded = true) Id: String,
        @Body profile: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Unit>



    @Headers("apikey: $SUPABASE_KEY")
    @POST("rest/v1/profiles?select=id")
    suspend fun createProfile(
        @Body profile: Map<String, @JvmSuppressWildcards Any?>
    ): Response<List<Profile>>

    @Headers("apikey: $SUPABASE_KEY")
    @GET("rest/v1/profiles?select=id,user_id,firstname,lastname,address,phone,photo")
    suspend fun getProfile(@Query("user_id", encoded = true) userId: String): Response<List<Profile>>
}
package com.example.up_piatnitskii.data.navigation

import SignUpViewModel
import com.example.practice_mobile.ui.screen.SignUpScreen
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.up_piatnitskii.data.screens.CreateNewPassword
import com.example.up_piatnitskii.data.screens.ForgotPassword
import com.example.up_piatnitskii.data.screens.OnboardScreen
import com.example.up_piatnitskii.data.screens.SignInScreen
import com.example.up_piatnitskii.data.screens.Verfication
import com.example.up_piatnitskii.data.viewModel.SignInViewModel


@Composable
fun NavigationApp(
                    navController: NavHostController,
                  signUpViewModel: SignUpViewModel,
                  signInViewModel: SignInViewModel,
                  context: Context
)
{
    NavHost(
        navController = navController,
        startDestination = "start_menu"

    ) {
        composable("start_menu") {
            OnboardScreen (
                onGetStartedClick = { navController.navigate("sign_up") },
            )
        }

        composable("sign_up") {
            SignUpScreen(
                viewModel = signUpViewModel,

                onRegistrationSuccess = {
                       navController.navigate("sign_in") {
                            popUpTo("sign_up") { inclusive = true }
                       }
                },
                onLoginClick = {
                    navController.navigate("sign_in")
                },
                onBackClick = {
                    navController.navigate("start_menu")
                }
            )
        }
        composable("ForgotPassword") {
            ForgotPassword(
                onOTPClick = {navController.navigate("Verification")},
            )
        }

        composable("Verification") {
            Verfication()
        }

        composable("CreateNewPassword") {
            CreateNewPassword()
        }

        composable("sign_in") {
            SignInScreen(
                viewModel = signInViewModel,
                onRegisterClick = {navController.navigate("sign_up")},
                onSignInClick = {navController.navigate("sign_up")},
                onForgotPasswordClick = {navController.navigate("ForgotPassword")},
            )
        }
    }
}
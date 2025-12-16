package com.example.up_piatnitskii

import SignUpViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.practice_mobile.ui.screen.SignUpScreen
import com.example.up_piatnitskii.data.Model.StudentApplication
import com.example.up_piatnitskii.data.navigation.NavigationApp
import com.example.up_piatnitskii.data.screens.SignInScreen
import com.example.up_piatnitskii.data.viewModel.SignInViewModel
import com.example.up_piatnitskii.ui.theme.UPPiatnitskiiTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    val signUpViewModel by viewModels<SignUpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val signUpViewModel = SignUpViewModel(StudentApplication.database.userDao())
        val signInViewModel = SignInViewModel(StudentApplication.database.userDao())

        enableEdgeToEdge()

        setContent {
            UPPiatnitskiiTheme {

                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    NavigationApp(navController = navController, signUpViewModel = signUpViewModel, signInViewModel = signInViewModel, context = context)
                }
            }
        }
    }
}

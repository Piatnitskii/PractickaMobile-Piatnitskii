package com.example.up_piatnitskii.data.navigation

import SignUpViewModel
import android.R.attr.type
import com.example.practice_mobile.ui.screen.SignUpScreen
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shoeshop.ui.viewmodel.HomeViewModel
import com.example.up_piatnitskii.data.screens.CartScreen
import com.example.up_piatnitskii.data.screens.CategoryProductsScreen
import com.example.up_piatnitskii.data.screens.CreateNewPassword
import com.example.up_piatnitskii.data.screens.ForgotPassword
import com.example.up_piatnitskii.data.screens.HomeScreen
import com.example.up_piatnitskii.data.screens.OnboardScreen
import com.example.up_piatnitskii.data.screens.ProductDetailScreen
import com.example.up_piatnitskii.data.screens.SignInScreen
import com.example.up_piatnitskii.data.screens.Verfication

import com.example.up_piatnitskii.data.viewModel.SignInViewModel


@Composable
fun NavigationApp(navController: NavHostController,
                  signUpViewModel: SignUpViewModel,
                  signInViewModel: SignInViewModel,
                  context: Context) {
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

        composable("sign_in") {
            SignInScreen(
                viewModel = signInViewModel,
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate("sign_up") },
                onSignInClick = { navController.navigate("home") },
                onForgotPasswordClick = { navController.navigate("ForgotPassword") }
            )
        }
        composable("Verivication") {
            Verfication()
        }


        composable("ForgotPassword") {
            ForgotPassword(
                onOTPClick = {navController.navigate("Verivication")},
                onBackClick = { navController.navigate("sign_in")  }
            )

        }
        composable("home") {
            HomeScreen(
                onProductClick = { product ->
                    // Навигация на экран товара
                    navController.navigate("product/${product.id}")
                },
                onCartClick = {
                    // Навигация на корзину
                    // navController.navigate("cart")
                },
                onSearchClick = {
                    // Навигация на поиск
                    // navController.navigate("search")
                },
                onCategoryClick = { categoryName ->
                    // Навигация на экран категории
                    navController.navigate("category/$categoryName")
                }
            )
        }

//        composable("cart") { backStackEntry ->
//            val homeBackStackEntry = remember(backStackEntry) {
//                navController.getBackStackEntry("home")
//            }
//            val homeViewModel: HomeViewModel = viewModel(homeBackStackEntry)
//            val cartViewModel: CartViewModel = viewModel(backStackEntry)
//
//            val state by cartViewModel.uiState.collectAsStateWithLifecycle()
//
//            CartScreen(
//                items = state.items,
//                isLoading = state.isLoading,
//                onBackClick = { navController.popBackStack() },
//                onIncrement = { item ->
//                    cartViewModel.increment(item)
//                    homeViewModel.refreshCartFlags()
//                },
//                onDecrement = { item ->
//                    cartViewModel.decrement(item)
//                    homeViewModel.refreshCartFlags()
//                },
//                onRemove = { item ->
//                    cartViewModel.remove(item)
//                    homeViewModel.refreshCartFlags()
//                },
//                onCheckoutClick = {
//                    navController.navigate("checkout")
//                }
//            )
//        }
        composable(
            route = "category/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryProductsScreen(
                categoryName = categoryName,
                onProductClick = { product ->
                    // Навигация на экран товара
                    navController.navigate("product/${product.id}")
                },
                onBackClick = { navController.popBackStack() },
                onCategorySelected = { newCategoryName ->
                    // Навигация на другую категорию
                    navController.navigate("category/$newCategoryName") {
                        // Очищаем стек чтобы не было много экранов категорий
                        popUpTo("category/{categoryName}") { inclusive = true }
                    }
                }
            )
        }

        // Добавьте новый маршрут для деталей товара
        composable(
            route = "product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onAddToCart = {
                },
                onToggleFavorite = {
                }
            )
        }


    }
}
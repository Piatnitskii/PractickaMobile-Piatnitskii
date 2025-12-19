package com.example.up_piatnitskii.data.screens

import android.util.Log
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_tomin.data.components.BackButton
import com.example.shoeshop.ui.viewmodel.HomeViewModel
import com.example.up_piatnitskii.data.Model.Product
import com.example.up_piatnitskii.data.components.ProductCard
import com.example.up_piatnitskii.ui.theme.BackgroundColor
import com.example.up_piatnitskii.ui.theme.RalewayTypography

import kotlinx.coroutines.delay
import kotlin.getOrDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    categoryName: String,
    onProductClick: (Product) -> Unit,
    onBackClick: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Загружаем товары категории
    val categoryProducts = remember(categoryName) {
        mutableStateOf<List<Product>>(emptyList())
    }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(categoryName) {
        // Если категорий нет - загружаем
        if (uiState.categories.isEmpty()) {
            viewModel.loadData()
            // Подождем пока загрузятся
            delay(500)
        }

        isLoading = true
        val result = viewModel.loadCategoryProducts(categoryName)
        if (result.isSuccess) {
            categoryProducts.value = result.getOrDefault(emptyList())
        } else {
            // Логируем ошибку
            Log.e("CategoryScreen", "Ошибка: ${result.exceptionOrNull()?.message}")
        }
        isLoading = false

        viewModel.selectCategory(categoryName)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF7F7F9) // rgba(247, 247, 249, 1) в HEX
                ),
                title = {
                    Text(
                        text = categoryName,
                        style = RalewayTypography.headingRegular32
                    )
                },
                navigationIcon = {
                    BackButton(onClick = onBackClick)
                }
            )
        },
        containerColor = BackgroundColor,
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(BackgroundColor)
            ) {
                // Полоска с категориями
                CategorySection(
                    categories = uiState.categories,
                    selectedCategory = categoryName,
                    onCategorySelected = { newCategoryName ->
                        onCategorySelected(newCategoryName)
                    },
                    padding = 20.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Товары категории в 2 колонки
                if (categoryProducts.value.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Нет товаров в категории",
                            style = RalewayTypography.bodyRegular14,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(categoryProducts.value) { product ->
                            ProductCard(
                                product = product,
                                onProductClick = { onProductClick(product) },
                                onFavoriteClick = { /* обработка избранного */ },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
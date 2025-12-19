// screens/HomeScreen.kt
package com.example.up_piatnitskii.data.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.data.components.ProductCard
import com.example.up_piatnitskii.data.Model.Category
import com.example.up_piatnitskii.data.Model.Product

import com.example.up_piatnitskii.ui.theme.AccentColor
import com.example.up_piatnitskii.ui.theme.RalewayTypography
import com.example.up_piatnitskii.ui.theme.TextColor

import com.example.shoeshop.ui.viewmodel.HomeViewModel
import com.example.up_piatnitskii.data.screens.ProfileScreen
import com.example.up_piatnitskii.data.viewModel.ProfileViewModel
import com.example.up_piatnitskii.data.viewModel.SupabaseClient
import com.example.up_piatnitskii.ui.theme.BackgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {} //  параметр для навигации на категорию
) {
    var selected by remember { mutableIntStateOf(0) }

    // Используем ViewModel для управления состоянием
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth().background(BackgroundColor)

            ) {
                // Фоновая картинка
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.vector_1789),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Контент меню поверх картинки
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Левая группа иконок
                    Row {
                        IconButton(onClick = { selected = 0 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.home),
                                contentDescription = "Home",
                                tint = if (selected == 0) AccentColor else Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { selected = 1 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.favorite),
                                contentDescription = "Favorites",
                                tint = if (selected == 1) AccentColor else Color.Black
                            )
                        }
                    }

                    // Центральная кнопка корзины
                    Box(
                        modifier = Modifier
                            .offset(y = (-20).dp)
                            .size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            onClick = { onCartClick() },
                            modifier = Modifier.size(56.dp),
                            containerColor = AccentColor,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(30.dp) // Задайте нужный радиус закругления
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.bag_2),
                                contentDescription = "Cart",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }


                    // Правая группа иконок
                    Row {
                        IconButton(onClick = { selected = 2 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.notification),
                                contentDescription = "Notification",
                                tint = if (selected == 2) AccentColor else Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { selected = 3 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "Profile",
                                tint = if (selected == 3) AccentColor else Color.Black
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        // Показываем индикатор загрузки
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // Показываем ошибку если есть
        uiState.errorMessage?.let { errorMessage ->
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                title = { Text("Ошибка загрузки") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.clearError() }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            // Верхняя панель с заголовком, поиском и настройками (только для главной вкладки)
            if (selected == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.home),
                        style = RalewayTypography.headingRegular32,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        textAlign = TextAlign.Center
                    )

                    // Строка с поиском и настройками
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Поле поиска
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                        ) {
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                placeholder = {
                                    Text(
                                        text = "Поиск...",
                                        style = RalewayTypography.bodyRegular14
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Поиск",
                                        tint = Color.Gray
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    // Прозрачные границы
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    disabledBorderColor = Color.Transparent,
                                    // Цвета фона
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    disabledContainerColor = Color.White
                                ),
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Иконка настроек
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(AccentColor)
                                .clickable { onSettingsClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sliders),
                                contentDescription = "Настройки",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Основной контент
            Box(modifier = Modifier.fillMaxSize()) {
                when (selected) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            // Секция: Категории
                            item {
                                CategorySection(
                                    categories = uiState.categories,
                                    selectedCategory = uiState.selectedCategory,
                                    onCategorySelected = { categoryName ->
                                        viewModel.selectCategory(categoryName)
                                        onCategoryClick(categoryName) // Навигация на экран категории
                                    },
                                    padding = 1.dp
                                )
                            }

                            // Секция: Популярное
                            item {
                                PopularSection(
                                    products = uiState.popularProducts,
                                    onProductClick = onProductClick,
                                    onFavoriteClick = { product ->
                                        // Обработка добавления в избранное
                                    }
                                )
                            }

                            // Секция: Акции
                            item {
                                PromotionsSection()
                            }
                        }
                    }
                    1 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Избранное",
                                style = RalewayTypography.headingRegular32
                            )
                        }
                    }
                    2 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Уведомления",
                                style = RalewayTypography.headingRegular32
                            )
                        }
                    }
                    3 -> {
                        val viewModel: ProfileViewModel=remember { ProfileViewModel(SupabaseClient()) }
                        ProfileScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    categories: List<Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    padding: Dp
) {
    Column {
        Text(
            text = stringResource(id = R.string.categories),
            style = RalewayTypography.bodyMedium16,
            modifier = Modifier.padding(bottom = 15.dp).padding(horizontal = padding)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category.name,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(10.dp)),
        color = if (isSelected) AccentColor else Color.White,
        contentColor = if (isSelected) Color.White else TextColor
    ) {
        Text(
            text = category,
            style = RalewayTypography.bodyRegular12.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 42.dp, vertical = 11.dp)
        )
    }
}

@Composable
private fun PopularSection(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit
) {
    Column {
        // Заголовок раздела
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top =12.dp ),
                text = stringResource(id = R.string.popular),
                style = RalewayTypography.bodyMedium16,
            )
            Text(
                text = "Все",
                style = RalewayTypography.bodyRegular12,
                color = AccentColor,
                modifier = Modifier.clickable {
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Проверяем, есть ли товары
        if (products.isEmpty()) {
            Text(
                text = "Нет товаров",
                style = RalewayTypography.bodyRegular14,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center
            )
        } else {
            // Список товаров
            LazyRow(
                modifier = Modifier.padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { onProductClick(product) },
                        onFavoriteClick = { onFavoriteClick(product) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun PromotionsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                text = stringResource(id = R.string.sales),
                style = RalewayTypography.bodyMedium16,
                modifier = Modifier.padding()
            )
            Text(
                text = "Все",
                style = RalewayTypography.bodyRegular12,
                color = AccentColor,
                modifier = Modifier.clickable {
                    // Навигация на все популярные товары
                }
            )
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable {  },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = painterResource(
                    R.drawable.frame_1000000849
                ),
                contentDescription = "Summer sale",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onProductClick = {},
        onCartClick = {},
        onSearchClick = {},
        onSettingsClick = {},
        onCategoryClick = {}
    )
}
// ui/screens/ProductDetailScreen.kt
package com.example.up_piatnitskii.data.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.up_piatnitskii.data.Model.Product

import com.example.up_piatnitskii.ui.theme.RalewayTypography
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.data.viewModel.ProductDetailViewModel
import com.example.up_piatnitskii.ui.theme.AccentColor
import com.example.up_piatnitskii.ui.theme.HintColor

import kotlin.text.lines

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onToggleFavorite: (Product) -> Unit = {} // не обязателен, вся логика внутри VM
) {
    val viewModel: ProductDetailViewModel = viewModel()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "",
                        style = RalewayTypography.bodyMedium16
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* можно добавить переход в корзину */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bag_2),
                            contentDescription = "Корзина",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Ошибка", color = Color.Red)
                        Text(text = error ?: "")
                        Button(onClick = { viewModel.loadProduct(productId) }) {
                            Text(text = "Повторить")
                        }
                    }
                }

                product != null -> {
                    ProductDetailContent(
                        product = product!!,
                        onAddToCart = onAddToCart,
                        onToggleFavorite = {
                            // ВАЖНО: здесь вызываем метод VM
                            viewModel.toggleFavorite(product!!)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Товар не найден")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(product.isFavorite) }

    Column(
        modifier = modifier
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = product.name,
                style = RalewayTypography.headingRegular26.copy(color = Color.Black),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = product.getFormattedPrice(),
                style = RalewayTypography.bodyRegular24.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = product.description,
                style = RalewayTypography.bodyRegular16.copy(color = Color.Black),
                lineHeight = 24.sp,
                color = HintColor,
                maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (product.description.lines().size > 3 || product.description.length > 150) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { isDescriptionExpanded = !isDescriptionExpanded }
                    ) {
                        Text(
                            text = if (isDescriptionExpanded) "Скрыть" else "Подробнее",
                            color = AccentColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp), // Расстояние между кнопками
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F5F5))
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Избранное",
                        tint = if (isFavorite) Color.Red else Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier
                        .height(52.dp)
                        .width(256.dp)
                        .weight(0.2f),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentColor,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bag_2),
                            contentDescription = "В корзину",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "В корзину",
                            style = RalewayTypography.bodyMedium16
                        )
                    }
                }
            }
        }

    }
}

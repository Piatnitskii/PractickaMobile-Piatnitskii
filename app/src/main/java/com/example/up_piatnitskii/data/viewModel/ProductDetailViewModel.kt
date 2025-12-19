package com.example.up_piatnitskii.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.up_piatnitskii.data.SessionManager
import com.example.up_piatnitskii.data.Model.Product
import com.example.up_piatnitskii.data.repository.FavouriteRepository
import com.example.up_piatnitskii.data.repository.ProductsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.getOrDefault
import kotlin.run

class ProductDetailViewModel(
    private val repository: ProductsRepository = ProductsRepository(),
    private val favouriteRepository: FavouriteRepository = FavouriteRepository()
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = repository.getProductById(productId)
                if (result.isSuccess) {
                    var loaded = result.getOrNull()

                    val userId = SessionManager.userId
                    if (userId != null && loaded != null) {
                        val favResult = favouriteRepository.isFavorite(userId, loaded.id)
                        if (favResult.isSuccess && favResult.getOrDefault(false)) {
                            loaded = loaded.copy(isFavorite = true)
                        }
                    }

                    _product.value = loaded
                } else {
                    _error.value = result.exceptionOrNull()?.message
                        ?: "Не удалось загрузить товар"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun toggleFavorite(product: Product) {
        Log.d("Favorite", "SessionManager.userId = ${SessionManager.userId}")
        val userId = SessionManager.userId ?: run {
            _error.value = "Пользователь не авторизован"
            return
        }

        viewModelScope.launch {
            val wasFavorite = product.isFavorite

            // только isFavorite меняем
            _product.value = product.copy(isFavorite = !wasFavorite)

            val result = if (!wasFavorite) {
                favouriteRepository.addFavorite(userId, product.id)
            } else {
                favouriteRepository.removeFavorite(userId, product.id)
            }

            if (result.isFailure) {
                _product.value = product
                _error.value =
                    "Не удалось обновить избранное: ${result.exceptionOrNull()?.message}"
            }
        }
    }
}

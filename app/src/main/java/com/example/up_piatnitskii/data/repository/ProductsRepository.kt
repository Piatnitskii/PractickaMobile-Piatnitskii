package com.example.up_piatnitskii.data.repository

import android.util.Log
import com.example.up_piatnitskii.data.RetrofitInstance
import com.example.up_piatnitskii.data.Model.Category
import com.example.up_piatnitskii.data.Model.Product
import java.util.Collections.emptyList
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.firstOrNull
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.take
import kotlin.getOrDefault
import kotlin.let
import kotlin.text.take


class ProductsRepository {
    private val productsService = RetrofitInstance.productsService
    private val categoriesService = RetrofitInstance.categoriesService

    companion object {
        private const val TAG = "ProductsRepository"
    }

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val response = productsService.getProducts()
            Log.d(TAG, "getAllProducts: код ${response.code()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "getAllProducts: получено ${body?.size ?: 0} товаров")
                body?.take(3)?.forEachIndexed { index, product ->
                    Log.d(TAG,
                        "Товар $index: " +
                                "id=${product.id}, " +
                                "name=${product.name}, " +
                                "price=${product.price}, " +
                                "categoryId=${product.categoryId}, " +
                                "isBestSeller=${product.isBestSeller}, " +
                                "description=${product.description.take(30)}..."
                    )
                }
                body?.firstOrNull()?.let { firstProduct ->
                    if (firstProduct.categoryId == null) {
                        Log.w(TAG, "ВНИМАНИЕ: categoryId = null! Проверьте модель Product")
                    }
                }
                Result.success(body ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Ошибка getAllProducts: ${response.code()}, $errorBody")
                Result.failure(kotlin.Exception("HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение getAllProducts: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getBestSellers(): Result<List<Product>> {
        return try {
            val allProductsResult = getAllProducts()
            if (allProductsResult.isSuccess) {
                val allProducts = allProductsResult.getOrDefault(emptyList())
                val bestSellers = allProducts.filter { it.isBestSeller }
                Log.d(TAG, "getBestSellers: из ${allProducts.size} товаров найдено ${bestSellers.size} бестселлеров")
                Result.success(bestSellers)
            } else {
                allProductsResult
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение getBestSellers: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = categoriesService.getCategories()
            Log.d(TAG, "getCategories: код ${response.code()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "getCategories: получено ${body?.size ?: 0} категорий")
                body?.forEach { category ->
                    Log.d(TAG, "Категория: id=${category.id}, name=${category.name}")
                }
                Result.success(body ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Ошибка getCategories: ${response.code()}, $errorBody")
                Result.failure(kotlin.Exception("HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение getCategories: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(categoryId: String): Result<List<Product>> {
        return try {
            val filter = "eq.$categoryId"
            Log.d(TAG, "Запрос товаров с фильтром: $filter")
            val response = productsService.getProductsByCategory(
                categoryId = filter
            )

            Log.d(TAG, "Ответ по категории: код ${response.code()}")
            Log.d(TAG, "Ответ по категории: успешно ${response.isSuccessful}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "Получено товаров категории: ${body?.size ?: 0}")
                Result.success(body ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Ошибка HTTP ${response.code()}: $errorBody")
                Result.failure(kotlin.Exception("Failed to load category products: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение в getProductsByCategory: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getProductById(productId: String): Result<Product> {
        return try {
            Log.d(TAG, "Запрос товара по ID: $productId")

            // Сначала получим все товары
            val allProductsResult = getAllProducts()

            if (allProductsResult.isSuccess) {
                val allProducts = allProductsResult.getOrDefault(emptyList())
                Log.d(TAG, "Всего товаров загружено: ${allProducts.size}")

                // Найдем товар по ID
                val product = allProducts.find { it.id == productId }

                if (product != null) {
                    Log.d(TAG, "Товар найден: ${product.name}, цена: ${product.price}, ID: ${product.id}")
                    Result.success(product)
                } else {
                    Log.e(TAG, "Товар с ID '$productId' не найден в списке. Доступные ID:")
                    allProducts.take(5).forEach { p ->
                        Log.d(TAG, "  - ${p.id}: ${p.name}")
                    }
                    Result.failure(kotlin.Exception("Товар с ID '$productId' не найден"))
                }
            } else {
                Log.e(TAG, "Не удалось загрузить список товаров для поиска по ID")
                Result.failure(kotlin.Exception("Не удалось загрузить данные"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение getProductById: ${e.message}", e)
            Result.failure(e)
        }
    }

}
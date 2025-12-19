package com.example.up_piatnitskii.data.service


import com.example.up_piatnitskii.data.Model.Category
import com.example.up_piatnitskii.data.Model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsService {

    @GET("products")
    suspend fun getProducts(
        @Query("select") select: String = "*",
        @Query("order") order: String = "title.asc"  // Измените на существующее поле
    ): Response<List<Product>>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("select") select: String = "*",
        @Query("category_id") categoryId: String,  // УБЕРИТЕ значение по умолчанию!
        @Query("order") order: String = "title.asc"
    ): Response<List<Product>>
}

interface CategoriesService {

    @GET("categories")
    suspend fun getCategories(
        @Query("select") select: String = "*",
        @Query("order") order: String = "title.asc"
    ): Response<List<Category>>
}

package com.example.up_piatnitskii.data.repository


import com.example.up_piatnitskii.data.RetrofitInstance
import com.example.up_piatnitskii.data.Model.FavouriteItem
import kotlin.collections.isNullOrEmpty

class FavouriteRepository {

    private val service = RetrofitInstance.favouriteService
    private val TAG = "FavouriteRepository"

    suspend fun addFavorite(userId: String, productId: String): Result<Unit> {
        return try {
            val response = service.addFavorite(
                FavouriteItem(productId = productId, userId = userId)
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    kotlin.Exception("HTTP ${response.code()}: ${response.errorBody()?.string()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavorite(userId: String, productId: String): Result<Unit> {
        return try {
            val response = service.removeFavorite(
                userIdFilter = "eq.$userId",
                productIdFilter = "eq.$productId"
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(
                kotlin.Exception(
                    "HTTP ${response.code()}: ${
                        response.errorBody()?.string()
                    }"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun isFavorite(userId: String, productId: String): Result<Boolean> {
        return try {
            val response = service.getFavorites(
                userIdFilter = "eq.$userId",
                productIdFilter = "eq.$productId"
            )
            if (response.isSuccessful) {
                Result.success(!response.body().isNullOrEmpty())
            } else {
                Result.failure(
                    kotlin.Exception("HTTP ${response.code()}: ${response.errorBody()?.string()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

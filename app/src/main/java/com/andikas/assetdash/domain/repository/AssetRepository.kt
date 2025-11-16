package com.andikas.assetdash.domain.repository

import com.andikas.assetdash.data.local.entity.TransactionEntity
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.domain.model.CoinDetail
import com.andikas.assetdash.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface AssetRepository {

    fun getCoinMarkets(): Flow<Resource<List<Coin>>>
    fun getCoinDetails(coinId: String): Flow<Resource<CoinDetail?>>

    fun getFavoriteCoins(): Flow<Resource<List<Coin>>>
    suspend fun updateFavoriteStatus(coinId: String, isFavorite: Boolean)

    fun getCoinById(coinId: String): Flow<Resource<Coin>>

    suspend fun addTransaction(transaction: TransactionEntity)
}
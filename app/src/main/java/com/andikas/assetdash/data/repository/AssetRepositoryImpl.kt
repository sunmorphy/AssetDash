package com.andikas.assetdash.data.repository

import com.andikas.assetdash.data.local.dao.AssetDao
import com.andikas.assetdash.data.local.mapper.toCoin
import com.andikas.assetdash.data.local.mapper.toCoinDetail
import com.andikas.assetdash.data.remote.ApiService
import com.andikas.assetdash.data.remote.mapper.toCoinDetailEntity
import com.andikas.assetdash.data.remote.mapper.toCoinEntity
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.domain.model.CoinDetail
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.repository.AssetRepository
import com.andikas.assetdash.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: AssetDao
) : AssetRepository {

    override fun getCoinMarkets(): Flow<Resource<List<Coin>>> = networkBoundResource(
        query = {
            dao.getCoins()
        },

        fetch = {
            val oldCache = dao.getCoins().first()
            val favoriteMap = oldCache.associate { it.id to it.isFavorite }

            val remoteData = apiService.getCoinMarkets()

            val newEntities = remoteData.map { dto ->
                dto.toCoinEntity().copy(
                    isFavorite = favoriteMap[dto.id] ?: false
                )
            }

            dao.deleteAllCoins()
            dao.insertCoins(newEntities)
        },

        map = { entityList ->
            entityList?.map { it.toCoin() }
        }
    )

    override fun getCoinDetails(coinId: String): Flow<Resource<CoinDetail?>> = networkBoundResource(
        query = {
            dao.getCoinDetailById(coinId)
        },

        fetch = {
            val remoteData = apiService.getCoinDetails(coinId = coinId)
            dao.insertCoinDetail(remoteData.toCoinDetailEntity())
        },

        map = { entity ->
            entity?.toCoinDetail()
        }
    )

    override fun getFavoriteCoins(): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading())
        try {
            dao.getFavoriteCoins().map { entityList ->
                Resource.Success(entityList.map { it.toCoin() })
            }.collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mengambil data favorit: ${e.message}"))
        }
    }

    override suspend fun updateFavoriteStatus(coinId: String, isFavorite: Boolean) {
        dao.updateFavoriteStatus(coinId, isFavorite)
    }

    override fun getCoinById(coinId: String): Flow<Resource<Coin>> = flow {
        emit(Resource.Loading())
        try {
            dao.getCoinById(coinId).map { entity ->
                if (entity != null) {
                    Resource.Success(entity.toCoin())
                } else {
                    Resource.Error("Data koin tidak ditemukan di cache.")
                }
            }.collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mengambil data koin: ${e.message}"))
        }
    }
}
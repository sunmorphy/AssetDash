package com.andikas.assetdash.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andikas.assetdash.data.local.entity.CoinDetailEntity
import com.andikas.assetdash.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

    @Query("SELECT * FROM coins")
    fun getCoins(): Flow<List<CoinEntity>>

    @Query("SELECT * FROM coins WHERE isFavorite = 1")
    fun getFavoriteCoins(): Flow<List<CoinEntity>>

    @Query("UPDATE coins SET isFavorite = :isFavorite WHERE id = :coinId")
    suspend fun updateFavoriteStatus(coinId: String, isFavorite: Boolean)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinDetail(coinDetail: CoinDetailEntity)

    @Query("SELECT * FROM coin_details WHERE id = :coinId")
    fun getCoinDetailById(coinId: String): Flow<CoinDetailEntity?>

    @Query("SELECT * FROM coins WHERE id = :coinId")
    fun getCoinById(coinId: String): Flow<CoinEntity?>
}
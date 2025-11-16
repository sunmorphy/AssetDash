package com.andikas.assetdash.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andikas.assetdash.data.local.dao.AssetDao
import com.andikas.assetdash.data.local.dao.TransactionDao
import com.andikas.assetdash.data.local.entity.CoinDetailEntity
import com.andikas.assetdash.data.local.entity.CoinEntity
import com.andikas.assetdash.data.local.entity.TransactionEntity

@Database(
    entities = [
        CoinEntity::class,
        CoinDetailEntity::class,
        TransactionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AssetDatabase : RoomDatabase() {

    abstract fun assetDao(): AssetDao
    abstract fun transactionDao(): TransactionDao
}
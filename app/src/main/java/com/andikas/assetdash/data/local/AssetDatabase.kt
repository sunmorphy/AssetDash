package com.andikas.assetdash.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andikas.assetdash.data.local.dao.AssetDao
import com.andikas.assetdash.data.local.entity.CoinDetailEntity
import com.andikas.assetdash.data.local.entity.CoinEntity

@Database(
    entities = [
        CoinEntity::class,
        CoinDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AssetDatabase : RoomDatabase() {

    abstract fun assetDao(): AssetDao
}
package com.andikas.assetdash.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_details")
data class CoinDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val description: String
)
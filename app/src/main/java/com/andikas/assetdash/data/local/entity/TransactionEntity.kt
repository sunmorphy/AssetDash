package com.andikas.assetdash.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coinId: String,
    val amount: Double,
    val pricePerCoin: Double,
    val date: Long = System.currentTimeMillis()
)
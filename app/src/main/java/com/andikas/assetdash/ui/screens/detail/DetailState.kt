package com.andikas.assetdash.ui.screens.detail

import com.andikas.assetdash.data.local.entity.TransactionEntity
import com.andikas.assetdash.domain.model.CoinDetail
import com.andikas.assetdash.domain.model.PricePoint

data class DetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val transactions: List<TransactionEntity> = emptyList(),
    val isFavorite: Boolean = false,
    val error: String? = null,
    val priceHistory: List<PricePoint> = emptyList(),
    val isChartLoading: Boolean = false
)
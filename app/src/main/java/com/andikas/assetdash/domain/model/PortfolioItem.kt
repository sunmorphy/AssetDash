package com.andikas.assetdash.domain.model

data class PortfolioItem(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Double,
    val totalAmount: Double,
    val avgBuyPrice: Double,
    val totalCurrentValue: Double,
    val totalProfitLoss: Double,
    val totalProfitLossPercentage: Double
)
package com.andikas.assetdash.data.remote.mapper

import com.andikas.assetdash.data.local.entity.CoinEntity
import com.andikas.assetdash.data.remote.dto.CoinDto
import com.andikas.assetdash.domain.model.Coin

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id ?: "",
        name = name ?: "Unknown",
        symbol = symbol ?: "?",
        image = image ?: "",
        currentPrice = currentPrice ?: 0.0,
        priceChangePercentage24h = priceChangePercentage24h ?: 0.0,
        isFavorite = false
    )
}

fun CoinDto.toCoinEntity(): CoinEntity {
    return CoinEntity(
        id = id ?: "",
        name = name ?: "Unknown",
        symbol = symbol ?: "?",
        image = image ?: "",
        currentPrice = currentPrice ?: 0.0,
        priceChangePercentage24h = priceChangePercentage24h ?: 0.0
    )
}
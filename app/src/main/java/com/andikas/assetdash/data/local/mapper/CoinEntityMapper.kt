package com.andikas.assetdash.data.local.mapper

import com.andikas.assetdash.data.local.entity.CoinEntity
import com.andikas.assetdash.domain.model.Coin

fun CoinEntity.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        image = image,
        currentPrice = currentPrice,
        priceChangePercentage24h = priceChangePercentage24h,
        isFavorite = isFavorite
    )
}
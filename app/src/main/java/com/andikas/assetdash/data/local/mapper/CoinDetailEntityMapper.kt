package com.andikas.assetdash.data.local.mapper

import com.andikas.assetdash.data.local.entity.CoinDetailEntity
import com.andikas.assetdash.domain.model.CoinDetail

fun CoinDetailEntity.toCoinDetail(): CoinDetail {
    return CoinDetail(
        id = id,
        name = name,
        symbol = symbol,
        image = image,
        description = description
    )
}
package com.andikas.assetdash.data.remote.mapper

import com.andikas.assetdash.data.local.entity.CoinDetailEntity
import com.andikas.assetdash.data.remote.dto.CoinDetailDto
import com.andikas.assetdash.domain.model.CoinDetail

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    return CoinDetail(
        id = id ?: "",
        name = name ?: "Unknown",
        symbol = symbol ?: "?",
        image = image?.large ?: "",
        description = description?.en
            ?: "Tidak ada deskripsi."
    )
}

fun CoinDetailDto.toCoinDetailEntity(): CoinDetailEntity {
    return CoinDetailEntity(
        id = id ?: "",
        name = name ?: "Unknown",
        symbol = symbol ?: "?",
        image = image?.large ?: "",
        description = description?.en ?: "Tidak ada deskripsi."
    )
}
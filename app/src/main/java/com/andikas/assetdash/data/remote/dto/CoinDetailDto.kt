package com.andikas.assetdash.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("image")
    val image: ImageDto?,
    @SerializedName("description")
    val description: DescriptionDto?
)

data class ImageDto(
    @SerializedName("large")
    val large: String?
)

data class DescriptionDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("en")
    val en: String?
)
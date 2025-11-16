package com.andikas.assetdash.data.remote

import com.andikas.assetdash.data.remote.dto.CoinDetailDto
import com.andikas.assetdash.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets")
    suspend fun getCoinMarkets(
        @Query("vs_currency") vsCurrency: String = "idr",
        @Query("ids") ids: String? = null
    ): List<CoinDto>

    @GET("coins/{id}")
    suspend fun getCoinDetails(
        @Path("id") coinId: String,
        @Query("localization") localization: Boolean = false,
        @Query("tickers") tickers: Boolean = false,
        @Query("market_data") marketData: Boolean = false,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false,
        @Query("sparkline") sparkline: Boolean = false
    ): CoinDetailDto

    companion object {
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
    }
}
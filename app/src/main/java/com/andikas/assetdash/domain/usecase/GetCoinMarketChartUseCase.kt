package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.domain.model.PricePoint
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.repository.AssetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinMarketChartUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<List<PricePoint>>> =
        repository.getCoinMarketChart(coinId)
}
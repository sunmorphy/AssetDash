package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.domain.model.CoinDetail
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.repository.AssetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinDetailsUseCase @Inject constructor(
    private val repository: AssetRepository
) {

    operator fun invoke(coinId: String): Flow<Resource<CoinDetail?>> =
        repository.getCoinDetails(coinId = coinId)
}
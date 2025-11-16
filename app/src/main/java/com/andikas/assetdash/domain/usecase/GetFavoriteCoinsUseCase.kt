package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.repository.AssetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCoinsUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> = repository.getFavoriteCoins()
}
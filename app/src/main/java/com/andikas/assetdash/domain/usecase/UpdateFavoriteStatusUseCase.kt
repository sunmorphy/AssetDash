package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.domain.repository.AssetRepository
import javax.inject.Inject

class UpdateFavoriteStatusUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(coinId: String, isFavorite: Boolean) {
        repository.updateFavoriteStatus(coinId, isFavorite)
    }
}
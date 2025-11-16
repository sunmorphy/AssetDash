package com.andikas.assetdash.ui.screens.detail

import com.andikas.assetdash.domain.model.CoinDetail

data class DetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)
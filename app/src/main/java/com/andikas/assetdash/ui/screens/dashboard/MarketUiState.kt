package com.andikas.assetdash.ui.screens.dashboard

import com.andikas.assetdash.domain.model.Coin

data class MarketUiState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: String? = null
)
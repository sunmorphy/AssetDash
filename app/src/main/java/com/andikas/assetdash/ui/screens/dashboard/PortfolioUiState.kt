package com.andikas.assetdash.ui.screens.dashboard

import com.andikas.assetdash.domain.model.PortfolioItem

data class PortfolioUiState(
    val isLoading: Boolean = false,
    val portfolio: List<PortfolioItem> = emptyList(),
    val totalNetWorth: Double = 0.0,
    val error: String? = null
)
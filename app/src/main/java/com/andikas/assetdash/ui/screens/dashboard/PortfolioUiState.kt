package com.andikas.assetdash.ui.screens.dashboard

import com.andikas.assetdash.domain.model.PortfolioItem

data class PortfolioUiState(
    val isLoading: Boolean = false,
    val portfolio: List<PortfolioItem> = emptyList(),
    val error: String? = null
)
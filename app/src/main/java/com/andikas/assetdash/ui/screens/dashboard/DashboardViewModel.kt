package com.andikas.assetdash.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.usecase.GetCoinMarketsUseCase
import com.andikas.assetdash.domain.usecase.GetFavoriteCoinsUseCase
import com.andikas.assetdash.domain.usecase.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val getCoinMarketsUseCase: GetCoinMarketsUseCase,
    private val getFavoriteCoinsUseCase: GetFavoriteCoinsUseCase
) : ViewModel() {

    val portfolioState: StateFlow<PortfolioUiState> =
        getPortfolioUseCase().map { resource ->
            when (resource) {
                is Resource.Loading -> PortfolioUiState(isLoading = true)
                is Resource.Success -> PortfolioUiState(portfolio = resource.data ?: emptyList())
                is Resource.Error -> PortfolioUiState(error = resource.message)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PortfolioUiState(isLoading = true)
        )

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    fun onTabSelected(index: Int) {
        _selectedTabIndex.value = index
    }

    val marketState: StateFlow<MarketUiState> = _selectedTabIndex.flatMapLatest { tabIndex ->
        val useCaseFlow = when (tabIndex) {
            0 -> getCoinMarketsUseCase()
            1 -> getFavoriteCoinsUseCase()
            else -> getCoinMarketsUseCase()
        }

        useCaseFlow.map { resource ->
            when (resource) {
                is Resource.Loading -> MarketUiState(
                    isLoading = true,
                    coins = marketState.value.coins
                )

                is Resource.Success -> MarketUiState(coins = resource.data ?: emptyList())
                is Resource.Error -> MarketUiState(
                    error = resource.message,
                    coins = resource.data ?: marketState.value.coins
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MarketUiState(isLoading = true)
    )
}
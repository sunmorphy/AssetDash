package com.andikas.assetdash.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.domain.model.CoinDetail
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.usecase.GetCoinByIdUseCase
import com.andikas.assetdash.domain.usecase.GetCoinDetailsUseCase
import com.andikas.assetdash.domain.usecase.GetCoinMarketChartUseCase
import com.andikas.assetdash.domain.usecase.GetTransactionsForCoinUseCase
import com.andikas.assetdash.domain.usecase.UpdateFavoriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getCoinByIdUseCase: GetCoinByIdUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    private val getTransactionsForCoinUseCase: GetTransactionsForCoinUseCase,
    private val getCoinMarketChartUseCase: GetCoinMarketChartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val coinId: StateFlow<String> = savedStateHandle.getStateFlow("coinId", "")

    private val coinDetailsFlow: Flow<Resource<CoinDetail?>> = coinId.flatMapLatest { id ->
        if (id.isNotBlank()) {
            getCoinDetailsUseCase(id)
        } else {
            flowOf(Resource.Error("Invalid coin ID"))
        }
    }

    private val favoriteStatusFlow: Flow<Resource<Coin>> = coinId.flatMapLatest { id ->
        if (id.isNotBlank()) {
            getCoinByIdUseCase(id)
        } else {
            flowOf(Resource.Error("Invalid coin ID"))
        }
    }

    private val transactionsFlow = coinId.flatMapLatest { id ->
        if (id.isNotBlank()) getTransactionsForCoinUseCase(id)
        else flowOf(emptyList())
    }

    private val chartFlow = coinId.flatMapLatest { id ->
        if (id.isNotBlank()) getCoinMarketChartUseCase(id)
        else flowOf(Resource.Error("Invalid ID"))
    }

    val state: StateFlow<DetailState> = combine(
        coinDetailsFlow,
        favoriteStatusFlow,
        transactionsFlow,
        chartFlow
    ) { detailsResource, favoriteResource, transactionsList, chart ->

        val coinData = detailsResource.data
        val isFavorite = (favoriteResource as? Resource.Success)?.data?.isFavorite ?: false

        val isLoading = detailsResource is Resource.Loading || favoriteResource is Resource.Loading

        val error = if (detailsResource is Resource.Error) detailsResource.message
        else if (favoriteResource is Resource.Error) favoriteResource.message
        else null

        val chartData = chart.data ?: emptyList()
        val isChartLoading = chart is Resource.Loading

        DetailState(
            isLoading = isLoading,
            coin = coinData,
            transactions = transactionsList,
            isFavorite = isFavorite,
            error = error,
            priceHistory = chartData,
            isChartLoading = isChartLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailState(isLoading = true)
    )

    fun onFavoriteToggle() {
        viewModelScope.launch {
            val currentId = coinId.value
            if (currentId.isNotBlank()) {
                val newStatus = !state.value.isFavorite
                updateFavoriteStatusUseCase(currentId, newStatus)
            }
        }
    }
}
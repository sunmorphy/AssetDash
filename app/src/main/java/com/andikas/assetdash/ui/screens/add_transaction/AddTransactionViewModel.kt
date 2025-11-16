package com.andikas.assetdash.ui.screens.add_transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andikas.assetdash.data.local.entity.TransactionEntity
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.usecase.AddTransactionUseCase
import com.andikas.assetdash.domain.usecase.GetCoinMarketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: String? = null
)

// State untuk event UI (misal: navigasi kembali)
sealed class UiEvent {
    object SaveSuccess : UiEvent()
    data class SaveError(var message: String) : UiEvent()
}

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val getCoinMarketsUseCase: GetCoinMarketsUseCase,
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _selectedCoin = MutableStateFlow<Coin?>(null)
    val selectedCoin: StateFlow<Coin?> = _selectedCoin.asStateFlow()

    var amount by mutableStateOf("")
    var pricePerCoin by mutableStateOf("")

    private val _coinListState = MutableStateFlow(CoinListState())
    val coinListState: StateFlow<CoinListState> = _coinListState.asStateFlow()

    private val _eventFlow = MutableStateFlow<UiEvent?>(null)
    val eventFlow: StateFlow<UiEvent?> = _eventFlow.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredCoins: StateFlow<List<Coin>> = combine(
        coinListState,
        _searchQuery
    ) { state, query ->
        if (query.isBlank()) {
            state.coins
        } else {
            state.coins.filter { coin ->
                coin.name.contains(query, ignoreCase = true) ||
                        coin.symbol.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        fetchCoinList()
    }

    private fun fetchCoinList() {
        getCoinMarketsUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _coinListState.value = CoinListState(isLoading = true)
                is Resource.Success -> _coinListState.value =
                    CoinListState(coins = resource.data ?: emptyList())

                is Resource.Error -> _coinListState.value = CoinListState(error = resource.message)
            }
        }.launchIn(viewModelScope)
    }

    fun onSaveTransaction() {
        val coin = selectedCoin.value
        val amountDouble = amount.toDoubleOrNull()
        val priceDouble = pricePerCoin.toDoubleOrNull()

        if (coin == null || amountDouble == null || priceDouble == null) {
            _eventFlow.value = UiEvent.SaveError("Input tidak valid")
            return
        }

        viewModelScope.launch {
            addTransactionUseCase(
                TransactionEntity(
                    coinId = coin.id,
                    amount = amountDouble,
                    pricePerCoin = priceDouble
                )
            )
            _eventFlow.value = UiEvent.SaveSuccess
        }
    }

    fun consumeEvent() {
        _eventFlow.value = null
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query != _selectedCoin.value?.name) {
            _selectedCoin.value = null
        }
    }

    fun onCoinSelected(coin: Coin) {
        _selectedCoin.value = coin
        _searchQuery.value = coin.name

        pricePerCoin = coin.currentPrice.toLong().toString()
    }
}
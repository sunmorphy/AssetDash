package com.andikas.assetdash.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.usecase.GetCoinMarketsUseCase
import com.andikas.assetdash.domain.usecase.GetFavoriteCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCoinMarketsUseCase: GetCoinMarketsUseCase,
    private val getFavoriteCoinsUseCase: GetFavoriteCoinsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        fetchData(_state.value.selectedTabIndex)
    }

    private fun fetchData(tabIndex: Int) {
        val useCaseFlow = when (tabIndex) {
            0 -> getCoinMarketsUseCase()
            1 -> getFavoriteCoinsUseCase()
            else -> getCoinMarketsUseCase()
        }

        useCaseFlow.onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        coins = resource.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = resource.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onTabSelected(index: Int) {
        _state.value = _state.value.copy(
            selectedTabIndex = index,
            error = null,
            coins = emptyList()
        )
        fetchData(index)
    }
}
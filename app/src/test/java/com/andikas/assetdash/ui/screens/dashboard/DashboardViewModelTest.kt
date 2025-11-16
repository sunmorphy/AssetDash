package com.andikas.assetdash.ui.screens.dashboard

import app.cash.turbine.test
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.usecase.GetCoinMarketsUseCase
import com.andikas.assetdash.domain.usecase.GetFavoriteCoinsUseCase
import com.andikas.assetdash.utils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCoinMarketsUseCase: GetCoinMarketsUseCase = mockk()
    private val getFavoriteCoinsUseCase: GetFavoriteCoinsUseCase = mockk()

    private lateinit var viewModel: DashboardViewModel

    @Test
    fun `when viewmodel inits, should load 'All Markets' successfully`() = runTest {
        val fakeMarketData = listOf(
            Coin(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                image = "",
                currentPrice = 0.0,
                priceChangePercentage24h = 0.0,
                isFavorite = false
            )
        )

        every { getCoinMarketsUseCase() } returns flow {
            emit(Resource.Loading())
            delay(1)
            emit(Resource.Success(fakeMarketData))
        }
        every { getFavoriteCoinsUseCase() } returns flow {
            emit(Resource.Loading())
            delay(1)
            emit(Resource.Success(emptyList()))
        }

        viewModel = DashboardViewModel(getCoinMarketsUseCase, getFavoriteCoinsUseCase)

        viewModel.state.test {
            assertEquals(true, awaitItem().isLoading)

            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(0, successState.selectedTabIndex)
            assertEquals(fakeMarketData, successState.coins)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when onTabSelected(1), should switch to and load 'Favorites'`() = runTest {
        val fakeMarketData = listOf(
            Coin(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                image = "",
                currentPrice = 0.0,
                priceChangePercentage24h = 0.0,
                isFavorite = false
            )
        )
        val fakeFavoriteData = listOf(
            Coin(
                id = "eth",
                name = "Ethereum",
                symbol = "ETH",
                image = "",
                currentPrice = 0.0,
                priceChangePercentage24h = 0.0,
                isFavorite = true
            )
        )

        every { getCoinMarketsUseCase() } returns flow {
            emit(Resource.Loading())
            delay(1)
            emit(Resource.Success(fakeMarketData))
        }
        every { getFavoriteCoinsUseCase() } returns flow {
            emit(Resource.Loading())
            delay(1)
            emit(Resource.Success(fakeFavoriteData))
        }

        viewModel = DashboardViewModel(getCoinMarketsUseCase, getFavoriteCoinsUseCase)

        viewModel.state.test {
            awaitItem()
            val marketState = awaitItem()
            assertEquals(fakeMarketData, marketState.coins)

            viewModel.onTabSelected(1)

            val tabChangeState = awaitItem()
            assertEquals(1, tabChangeState.selectedTabIndex)
            assertEquals(false, tabChangeState.isLoading)

            val favoriteLoadingState = awaitItem()
            assertEquals(true, favoriteLoadingState.isLoading)
            assertEquals(1, favoriteLoadingState.selectedTabIndex)

            val favoriteSuccessState = awaitItem()
            assertEquals(false, favoriteSuccessState.isLoading)
            assertEquals(1, favoriteSuccessState.selectedTabIndex)
            assertEquals(fakeFavoriteData, favoriteSuccessState.coins)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
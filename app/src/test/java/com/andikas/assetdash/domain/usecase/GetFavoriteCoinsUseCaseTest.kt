package com.andikas.assetdash.domain.usecase

import app.cash.turbine.test
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.domain.model.Resource
import com.andikas.assetdash.domain.repository.AssetRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetFavoriteCoinsUseCaseTest {
    private lateinit var repository: AssetRepository
    private lateinit var useCase: GetFavoriteCoinsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetFavoriteCoinsUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        val fakeCoin = Coin(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            image = "",
            currentPrice = 0.0,
            priceChangePercentage24h = 0.0,
            isFavorite = true
        )
        val fakeData = listOf(fakeCoin)
        val fakeResource = Resource.Success(fakeData)

        every { repository.getFavoriteCoins() } returns flowOf(fakeResource)

        useCase().test {
            val emittedItem = awaitItem()

            assertEquals(true, emittedItem is Resource.Success)

            assertEquals(fakeData, emittedItem.data)

            awaitComplete()
        }
    }
}
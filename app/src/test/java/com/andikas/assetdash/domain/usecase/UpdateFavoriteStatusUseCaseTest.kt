package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.domain.repository.AssetRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateFavoriteStatusUseCaseTest {

    private lateinit var repository: AssetRepository

    private lateinit var useCase: UpdateFavoriteStatusUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true) // 'relaxed = true' agar tak perlu mock semua fungsi
        useCase = UpdateFavoriteStatusUseCase(repository)
    }

    @Test
    fun `invoke should call repository updateFavoriteStatus with correct parameters`() = runTest {
        val testCoinId = "bitcoin"
        val testIsFavorite = true

        useCase(testCoinId, testIsFavorite)

        coVerify(exactly = 1) {
            repository.updateFavoriteStatus(
                coinId = testCoinId,
                isFavorite = testIsFavorite
            )
        }
    }
}
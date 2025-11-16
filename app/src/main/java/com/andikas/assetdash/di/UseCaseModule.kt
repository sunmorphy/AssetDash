package com.andikas.assetdash.di

import com.andikas.assetdash.domain.repository.AssetRepository
import com.andikas.assetdash.domain.usecase.GetCoinByIdUseCase
import com.andikas.assetdash.domain.usecase.GetCoinDetailsUseCase
import com.andikas.assetdash.domain.usecase.GetCoinMarketsUseCase
import com.andikas.assetdash.domain.usecase.GetFavoriteCoinsUseCase
import com.andikas.assetdash.domain.usecase.UpdateFavoriteStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetCoinMarketsUseCase(
        repository: AssetRepository
    ): GetCoinMarketsUseCase {
        return GetCoinMarketsUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCoinDetailsUseCase(
        repository: AssetRepository
    ): GetCoinDetailsUseCase {
        return GetCoinDetailsUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetFavoriteCoinsUseCase(
        repository: AssetRepository
    ): GetFavoriteCoinsUseCase {
        return GetFavoriteCoinsUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateFavoriteStatusUseCase(
        repository: AssetRepository
    ): UpdateFavoriteStatusUseCase {
        return UpdateFavoriteStatusUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCoinByIdUseCase(
        repository: AssetRepository
    ): GetCoinByIdUseCase {
        return GetCoinByIdUseCase(repository)
    }
}
package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.data.local.entity.TransactionEntity
import com.andikas.assetdash.domain.repository.AssetRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(transaction: TransactionEntity) {
        repository.addTransaction(transaction)
    }
}
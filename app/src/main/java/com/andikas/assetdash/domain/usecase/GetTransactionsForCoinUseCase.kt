package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.data.local.dao.TransactionDao
import com.andikas.assetdash.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsForCoinUseCase @Inject constructor(
    private val transactionDao: TransactionDao
) {
    operator fun invoke(coinId: String): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsForCoin(coinId)
}
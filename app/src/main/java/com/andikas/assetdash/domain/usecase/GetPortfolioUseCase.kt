package com.andikas.assetdash.domain.usecase

import com.andikas.assetdash.data.local.dao.AssetDao
import com.andikas.assetdash.data.local.dao.TransactionDao
import com.andikas.assetdash.domain.model.PortfolioItem
import com.andikas.assetdash.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
    private val assetDao: AssetDao,
    private val transactionDao: TransactionDao
) {

    operator fun invoke(): Flow<Resource<List<PortfolioItem>>> = flow {
        emit(Resource.Loading())

        try {
            val marketDataFlow = assetDao.getCoins()
            val transactionsFlow = transactionDao.getAllTransactions()

            combine(marketDataFlow, transactionsFlow) { marketList, transactionList ->

                val marketPriceMap = marketList.associateBy { it.id }
                val transactionsByCoin = transactionList.groupBy { it.coinId }

                val portfolioItems = transactionsByCoin.mapNotNull { (coinId, transactions) ->
                    val marketCoin = marketPriceMap[coinId] ?: return@mapNotNull null

                    var totalAmount = 0.0
                    var totalCost = 0.0
                    transactions.forEach { tx ->
                        totalAmount += tx.amount
                        totalCost += tx.amount * tx.pricePerCoin
                    }

                    if (totalAmount == 0.0) {
                        return@mapNotNull null
                    }

                    val avgBuyPrice = totalCost / totalAmount
                    val totalCurrentValue = totalAmount * marketCoin.currentPrice
                    val totalProfitLoss = totalCurrentValue - totalCost
                    val totalProfitLossPercentage =
                        if (totalCost > 0) (totalProfitLoss / totalCost) * 100 else 0.0

                    PortfolioItem(
                        id = coinId,
                        name = marketCoin.name,
                        symbol = marketCoin.symbol,
                        image = marketCoin.image,
                        currentPrice = marketCoin.currentPrice,
                        totalAmount = totalAmount,
                        avgBuyPrice = avgBuyPrice,
                        totalCurrentValue = totalCurrentValue,
                        totalProfitLoss = totalProfitLoss,
                        totalProfitLossPercentage = totalProfitLossPercentage
                    )
                }

                portfolioItems

            }.collect { calculatedPortfolio ->
                emit(Resource.Success(calculatedPortfolio))
            }

        } catch (e: Exception) {
            emit(Resource.Error("Gagal menghitung portofolio: ${e.message}"))
        }
    }
}
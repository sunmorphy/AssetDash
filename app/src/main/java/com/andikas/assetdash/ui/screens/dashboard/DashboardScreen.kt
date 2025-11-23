package com.andikas.assetdash.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.ui.components.PortfolioCard
import com.andikas.assetdash.utils.formatRupiah

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onCoinClick: (String) -> Unit,
    onAddTransactionClick: () -> Unit
) {
    val portfolioState by viewModel.portfolioState.collectAsState()
    val marketState by viewModel.marketState.collectAsState()
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

    val tabTitles = listOf("Semua Aset", "Favorit")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Total Kekayaan Anda",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
                Text(
                    text = formatRupiah(portfolioState.totalNetWorth),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            PortfolioSection(state = portfolioState)

            SecondaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { viewModel.onTabSelected(index) },
                        text = { Text(title) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(marketState.coins) { coin ->
                        CoinListItem(coin = coin, onItemClick = onCoinClick)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (marketState.error != null) {
                    Text(
                        text = marketState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                if (marketState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun PortfolioSection(state: PortfolioUiState) {
    if (state.isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    if (state.error != null) {
        Text(
            text = state.error,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.portfolio) { portfolioItem ->
            PortfolioCard(item = portfolioItem)
        }
    }
}

@Composable
fun CoinListItem(
    coin: Coin,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(coin.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${coin.name} (${coin.symbol.uppercase()})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${coin.currentPrice}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = "%.2f%% (24j)".format(coin.priceChangePercentage24h),
                color = if (coin.priceChangePercentage24h >= 0) Color(0xFF00C853) else Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
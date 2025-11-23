package com.andikas.assetdash.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.andikas.assetdash.data.local.entity.TransactionEntity
import com.andikas.assetdash.ui.components.ExpandableText
import com.andikas.assetdash.ui.components.PriceChart
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.coin?.name ?: "Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onFavoriteToggle() }) {
                        Icon(
                            imageVector = if (state.isFavorite) {
                                Icons.Filled.Star
                            } else {
                                Icons.Outlined.StarBorder
                            },
                            contentDescription = "Favorite",
                            tint = if (state.isFavorite) Color(0xFFFFD700) else Color.Gray
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            state.coin?.let { coin ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = coin.image,
                        contentDescription = coin.name,
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${coin.name} (${coin.symbol.uppercase()})",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (state.isChartLoading) {
                        Box(modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    } else if (state.priceHistory.isNotEmpty()) {
                        Text(
                            text = "Grafik Harga (7 Hari)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        PriceChart(
                            dataPoints = state.priceHistory,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    ExpandableText(
                        htmlDescription = coin.description
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Riwayat Transaksi",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (state.transactions.isEmpty()) {
                        Text("Belum ada transaksi.", color = Color.Gray)
                    } else {
                        state.transactions.forEach { tx ->
                            TransactionItem(tx)
                            HorizontalDivider()
                        }
                    }
                }
            }

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun TransactionItem(tx: TransactionEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Beli",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00C853)
            )
            Text(
                text = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                    .format(tx.date),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "${tx.amount} Koin")
            Text(
                text = "@ Rp ${java.text.DecimalFormat("#,###").format(tx.pricePerCoin)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
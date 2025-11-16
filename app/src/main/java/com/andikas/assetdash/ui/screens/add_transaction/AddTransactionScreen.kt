package com.andikas.assetdash.ui.screens.add_transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.andikas.assetdash.domain.model.Coin
import com.andikas.assetdash.ui.components.RupiahTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val coinListState by viewModel.coinListState.collectAsState()
    val event = viewModel.eventFlow.collectAsState().value

    val selectedCoin by viewModel.selectedCoin.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredCoins by viewModel.filteredCoins.collectAsState()

    LaunchedEffect(event) {
        if (event is UiEvent.SaveSuccess) {
            viewModel.consumeEvent()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Transaksi") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CoinSelector(
                filteredCoins = filteredCoins,
                isLoading = coinListState.isLoading,
                searchQuery = searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                onCoinSelected = viewModel::onCoinSelected
            )

            OutlinedTextField(
                value = viewModel.amount,
                onValueChange = { viewModel.amount = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Jumlah Aset (cth: 0.5)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            RupiahTextField(
                value = viewModel.pricePerCoin,
                onValueChange = { viewModel.pricePerCoin = it },
                label = "Harga Beli per Koin",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onSaveTransaction() },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCoin != null && viewModel.amount.isNotBlank() && viewModel.pricePerCoin.isNotBlank()
            ) {
                Text("SIMPAN")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinSelector(
    filteredCoins: List<Coin>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onCoinSelected: (Coin) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                readOnly = false,
                label = { Text("Koin") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
            )

            if (filteredCoins.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filteredCoins.forEach { coin ->
                        DropdownMenuItem(
                            text = { Text("${coin.name} (${coin.symbol.uppercase()})") },
                            onClick = {
                                onCoinSelected(coin)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
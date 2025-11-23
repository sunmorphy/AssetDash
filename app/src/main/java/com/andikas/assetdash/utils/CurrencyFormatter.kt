package com.andikas.assetdash.utils

import java.text.DecimalFormat

fun formatRupiah(amount: Double): String {
    val formatter = DecimalFormat("#,###")
    return "Rp ${formatter.format(amount.toLong()).replace(',', '.')}"
}

fun formatAmount(amount: Double, symbol: String): String {
    return "%.4f %s".format(amount, symbol.uppercase())
}
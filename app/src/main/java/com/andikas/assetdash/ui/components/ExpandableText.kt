package com.andikas.assetdash.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat

@Composable
fun ExpandableText(
    htmlDescription: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val text = remember(htmlDescription) {
        HtmlCompat.fromHtml(htmlDescription, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
    }

    if (text.isBlank()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
        )

        Text(
            text = if (isExpanded) "Show Less" else "Show More",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { isExpanded = !isExpanded }
        )
    }
}
package com.andikas.assetdash.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

class ThousandSeparatorVisualTransformation : VisualTransformation {

    private val formatter = DecimalFormat("#,###")

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        val formattedText = try {
            val number = originalText.toLongOrNull()
            if (number != null) {
                formatter.format(number).replace(',', '.')
            } else {
                originalText
            }
        } catch (_: Exception) {
            originalText
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val originalSubString = originalText.take(offset)
                val formattedSubString = try {
                    formatter.format(originalSubString.toLongOrNull() ?: 0L).replace(',', '.')
                } catch (_: Exception) { originalSubString }

                return formattedText.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return originalText.length
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}
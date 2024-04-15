package ru.netology.nmedia.util

import kotlin.math.floor

fun amountShow(number: Int): String =
    when {
        number >= 1_000_000 && (number / 100_000) % 10 == 0 -> (number / 1_000_000).toString() + "M"
        number >= 1_000_000 -> (floor(number.toDouble() / 100_000) / 10).toString() + "M"
        number >= 10_000 || (number >= 1_000 && (number / 100) % 10 == 0) -> (number / 1_000).toString() + "K"
        number >= 1_000 -> (floor(number.toDouble() / 100) / 10).toString() + "K"
        else -> number.toString()
    }
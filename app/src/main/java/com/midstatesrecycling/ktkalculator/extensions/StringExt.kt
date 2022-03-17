package com.midstatesrecycling.ktkalculator.extensions

fun String.toDoubleValue(): Double {
    val digits = this.filter { it.isDigit() || it == '.' }
    return digits.toDoubleOrNull() ?: 0.0
}
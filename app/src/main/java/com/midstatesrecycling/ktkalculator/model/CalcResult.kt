package com.midstatesrecycling.ktkalculator.model

data class CalcResult (
    val finalEstimatedValue: Double = 0.0,
    val totalWeight: Double = 0.0,
    val estimatedPureGold: Double = 0.0,
    val estimatedPayablePureGold: Double = 0.0,
    val estimatedPurityPercentage: Double = 0.0,
    val estimatedPurityKarat: Double = 0.0,

    val accountability: Double = 0.0,
    val treatmentCharge: Double = 0.0
) {
    companion object {
        val EMPTY = CalcResult()
    }
}
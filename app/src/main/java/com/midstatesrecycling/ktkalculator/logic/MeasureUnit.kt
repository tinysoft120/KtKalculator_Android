package com.midstatesrecycling.ktkalculator.logic

enum class MeasureUnit(val unitName: String) {
    DWT("D.W.T"),
    GRAM("Grams"),
    TROY("Troy Oz"),
}

//enum class MeasureUnit {
//    DWT,
//    GRAM,
//    TROY,
//}
//
//fun MeasureUnit.name(): String {
//    return when (this) {
//        MeasureUnit.DWT -> "D.W.T"
//        MeasureUnit.GRAM -> "Grams"
//        MeasureUnit.TROY -> "Troy Oz"
//    }
//}
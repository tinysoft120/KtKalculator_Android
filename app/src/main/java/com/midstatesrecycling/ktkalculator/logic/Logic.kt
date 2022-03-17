package com.midstatesrecycling.ktkalculator.logic

import com.midstatesrecycling.ktkalculator.adapter.EstimateItem
import com.midstatesrecycling.ktkalculator.extensions.toDoubleValue
import com.midstatesrecycling.ktkalculator.model.CalcResult
import com.midstatesrecycling.ktkalculator.util.LogU.NSLog
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object Logic {
    // calculateDataFromFormula
    fun calculateEstimateValue(item: EstimateItem.DataItem, unit: MeasureUnit, goldPrice: Double): Double {
        val aryDWT: List<Double> = listOf(0.0200, 0.0234, 0.0277, 0.0295, 0.0359, 0.0436, 0.0481)
        val aryGrams: List<Double> = listOf(0.012863, 0.015038, 0.017829, 0.018958, 0.023137, 0.028020, 0.030912)
        val aryTroy: List<Double> = listOf(0.400, 0.468, 0.554, 0.59, 0.718, 0.872, 0.962)
        //val aryTroy: List<Double> = listOf(0.394, 0.468, 0.544, 0.59, 0.704, 0.872, 0.962)

        val unitValue = when (unit) {
            MeasureUnit.DWT -> aryDWT[item.index]
            MeasureUnit.GRAM -> aryGrams[item.index]
            MeasureUnit.TROY -> aryTroy[item.index]
        }

        val floatTemp = item.quantity * unitValue * goldPrice
        val tempStr = String.format("%.2f", floatTemp)
        return tempStr.toDoubleValue()  // floatTemp
    }

    fun calculateResult(goldItems: List<EstimateItem.DataItem>, measureUnit: MeasureUnit, goldPrice: Double): CalcResult {
        // result values
        var finalEstimate = 0.0
        var totalWeight = 0.0
        var displayKaratPurity = 0.0
        var estPayablePure = 0.0 // For  Estimated_Payable_Pure_Gold_Content
        var estPercentage = 0.0

        // Estimated Refining Terms values
        var accountability = 0.0
        var treatment = 0.0

        val arrPurity = listOf(0.396, 0.479, 0.563, 0.595, 0.729, 0.897, 0.999)
        var purityWeight = 0.0
        val calcPurityWeight = mutableListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        goldItems.map { item ->
            val i = item.id
            calcPurityWeight[i] = item.quantity * arrPurity[i]
            purityWeight += calcPurityWeight[i]
            totalWeight += item.quantity
        }

        if (purityWeight == 0.0) {
            return CalcResult.EMPTY
        }

        val karatPurityDWT = purityWeight / 20.0         // weight in purity for DWT
        val karatPurityGrams = purityWeight / 31.1035    // weight in purity for Grams
        val karatPurityTroy = purityWeight               // weight in purity for Troy

        totalWeight = roundedValueForEstimatedValue(totalWeight)

        NSLog("   total weight  == == === =%f", totalWeight)
        NSLog(" before long_KaratPurity_DWT == == === =%f", karatPurityDWT)
        NSLog(" before long_KaratPurity_Grams == == === =%f", karatPurityGrams)
        NSLog(" before long_KaratPurity_Troy == == === =%f", karatPurityTroy)

        val roundedPurityDWT = roundedValue(karatPurityDWT)
        val roundedPurityGrams = roundedValue(karatPurityGrams)
        val roundedPurityTroy = roundedValue(karatPurityTroy)

        NSLog("long_KaratPurity_DWT == == === =%f", roundedPurityDWT)
        NSLog("long_KaratPurity_Grams == == === =%f", roundedPurityGrams)
        NSLog("long_KaratPurity_Troy == == === =%f", roundedPurityTroy)


        ///  For Final Estimated Value After All Refining Charges-

        when (measureUnit) {
            MeasureUnit.DWT -> {
                totalWeight /= 20.0
                displayKaratPurity = karatPurityDWT

                if (roundedPurityDWT > 0 && roundedPurityDWT <= 5.0) {
                    accountability = 97.0
                    finalEstimate =
                        (.97 * goldPrice * karatPurityDWT) - 100
                    finalEstimate =
                        roundedValueForEstimatedValue(finalEstimate)
                    NSLog("Estimated_Value_AfterRefining===%.2f", finalEstimate)
                    treatment = 100.0
                } else if (roundedPurityDWT > 5.0 && roundedPurityDWT <= 30.0) {
                    accountability = 98.0
                    finalEstimate = (.98 * goldPrice * karatPurityDWT) - 50
                    finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityDWT > 30.0 && roundedPurityDWT <= 60.0) {
                    accountability = 98.5
                    finalEstimate =
                        .985 * goldPrice * karatPurityDWT - 50
                    finalEstimate =
                        roundedValueForEstimatedValue(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityDWT > 60 && roundedPurityDWT <= 110) {
                    accountability = 98.5
                    finalEstimate =
                        .985 * goldPrice * karatPurityDWT
                    finalEstimate =
                        roundedValueForEstimatedValue(finalEstimate)
                    treatment = -1.0 //"Waived"


                } else {
                    if (roundedPurityDWT == 0.0) {
                        accountability = 0.0
                        treatment = 0.0
                        NSLog("Called in DWTTTTTTTTT")
                    } else {
                        accountability = 99.0
                        finalEstimate = (.99 * goldPrice * karatPurityDWT) - (totalWeight * 0.75)
                        finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                        treatment = roundedValue(totalWeight * 0.75)
                    }


                }
                NSLog("est value  from DWT = == = %f", finalEstimate)

                ///// FOr  Estimated_Payable_Pure_Gold_Content
                estPayablePure = karatPurityDWT * accountability / 100.0
                estPayablePure = roundedValueForEstimatedValueUptoTwoValue(estPayablePure)
                NSLog("est value  from dwt after   = == = %.2f", estPayablePure)
                NSLog("str value  dwt=== %f", accountability)

                //Calculate Percentage
                estPercentage = karatPurityDWT / totalWeight
                estPercentage *= 100
                estPercentage = roundedValueForEstimatedValueForPercentage(estPercentage)
                //Percentage = RoundedValueForEstimated_ValueUptoTwo_Value(Percentage)
                NSLog("Percentage  DWT=============>>>>>>>>%.2f", estPercentage)

            }
            MeasureUnit.GRAM -> {
                totalWeight /= 31.1035
                displayKaratPurity = karatPurityGrams

                if (roundedPurityGrams > 0 && roundedPurityGrams <= 5.0) {
                    accountability = 97.0
                    finalEstimate =
                        .97 * goldPrice * karatPurityGrams - 100
                    finalEstimate =
                        roundedValueForEstimatedValue(finalEstimate)
                    treatment = 100.0

                } else if (roundedPurityGrams > 5.0 && roundedPurityGrams <= 30.0) {
                    accountability = 98.0
                    finalEstimate =
                        .98 * goldPrice * karatPurityGrams - 50
                    finalEstimate =
                        roundedValueForEstimatedValue(finalEstimate)
                    treatment = 50.0

                } else if (roundedPurityGrams > 30 && roundedPurityGrams <= 60) {
                    accountability = 98.5
                    finalEstimate = 0.985 * goldPrice * karatPurityGrams - 50
                    finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                    treatment = 50.00

                } else if (roundedPurityGrams > 60 && roundedPurityGrams <= 110) {
                    accountability = 98.5
                    finalEstimate = 0.985 * goldPrice * karatPurityGrams
                    finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                    treatment = -1.0 //"Waived"

                } else {
                    if (roundedPurityGrams == 0.0) {
                        accountability = 0.0
                        treatment = 0.0
                        NSLog("Called in Gramsss")

                    } else {
                        accountability = 99.0
                        finalEstimate = (0.99 * goldPrice * karatPurityGrams) - (totalWeight * 0.75)
                        finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                        treatment = roundedValue(totalWeight * 0.75)
                    }

                }
                NSLog("est value  from grams = == = %f", finalEstimate)
                ///// FOr  Estimated_Payable_Pure_Gold_Content

                estPayablePure = karatPurityGrams * accountability / 100.0
                estPayablePure = roundedValueForEstimatedValueUptoTwoValue(estPayablePure)

                NSLog("est value  from grams after   = == = %.2f", estPayablePure)

                //NSLog("est value  from grams after   = == = %f",Estimated_Payable_Pure_Gold_Content)
                NSLog("str value  grams=== %f", accountability)

                //Percentage
                estPercentage = karatPurityGrams / totalWeight
                estPercentage *= 100.0
                estPercentage = roundedValueForEstimatedValueForPercentage(estPercentage)
                NSLog("Percentage grams =============>>>>>>>>%f", estPercentage)
            }
            MeasureUnit.TROY -> {
                displayKaratPurity = karatPurityTroy

                if (roundedPurityTroy > 0 && roundedPurityTroy <= 5.0) {
                    accountability = 97.0
                    finalEstimate = .97 * goldPrice * karatPurityTroy - 100
                    finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                    treatment = 100.0

                } else if (roundedPurityTroy > 5.0 && roundedPurityTroy <= 30.0) {
                    accountability = 98.0
                    finalEstimate = .98 * goldPrice * karatPurityTroy - 50
                    finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityTroy > 30 && roundedPurityTroy <= 60) {
                    accountability = 98.5
                    finalEstimate = .985 * goldPrice * karatPurityTroy - 50
                    finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityTroy > 60 && roundedPurityTroy <= 110) {
                    accountability = 98.5
                    finalEstimate = .985 * goldPrice * karatPurityTroy
                    finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                    treatment = -1.0 // "Waived"


                } else {
                    if (roundedPurityTroy == 0.0) {
                        accountability = 0.0
                        treatment = 0.0
                        NSLog("Called in Troyyyyyyyyy")
                    } else {
                        accountability = 99.0
                        finalEstimate = (.99 * goldPrice * karatPurityTroy) - (totalWeight * 0.75)
                        finalEstimate = roundedValueForEstimatedValue(finalEstimate)
                        treatment = roundedValue(totalWeight * 0.75)
                    }

                }

                NSLog("est value  from troz = == = %f", finalEstimate)
                ///// FOr  Estimated_Payable_Pure_Gold_Content

                estPayablePure = karatPurityTroy * accountability / 100.0
                estPayablePure = roundedValueForEstimatedValueUptoTwoValue(estPayablePure)
                NSLog("est value  from troy after   = == = %f", estPayablePure)

                //Calculate Percentage
                estPercentage = karatPurityTroy / totalWeight
                estPercentage *= 100.0
                estPercentage = roundedValueForEstimatedValueForPercentage(estPercentage)
                NSLog("Percentage  Troy=============>>>>>>>>%f", estPercentage)
            }
        }

        //estimatedValue_in_Kformat
        var estKarat = (24.0 * estPercentage) / 100.0
        estKarat = roundedValueForEstimatedValueUptoTwoValue(estKarat)
        NSLog("estimated value in kFormat %.2f", estKarat)
        NSLog("str value  grams=== %f", accountability)

        return CalcResult(
            finalEstimate,
            totalWeight,
            displayKaratPurity,
            estPayablePure,
            estPercentage,
            estKarat,

            accountability,
            treatment,
        )
    }

    fun displayDecimalText(decimal: Double): String {
        // ex: 24234.20
        val df = DecimalFormat("0.00")
        return df.format(decimal)
    }

    fun roundedValue(FloatValue: Double, decimalCount: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.minimumFractionDigits = decimalCount
        numberFormat.maximumFractionDigits = decimalCount
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue)
    }

    fun roundedShort(FloatValue: Double, decimalCount: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = decimalCount
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue)
    }

    private fun roundedValue(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    private fun roundedValueForEstimatedValue(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 3
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    private fun roundedValueForEstimatedValueUptoTwoValue(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 3
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    private fun roundedValueForEstimatedValueForPercentage(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 4
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    /**
     *  END from KalculatorViewController
     * */
}
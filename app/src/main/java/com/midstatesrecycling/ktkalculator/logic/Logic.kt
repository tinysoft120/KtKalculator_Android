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
//    // original
//    var setTodayTable: Boolean = false
//    var isPriceChanged: Boolean = false
//    var Temp: Double = 0.0
//
//    // Global variables
//    var long_GoldRate: Double = 0.0
//    var long_KaratPurity_DWT: Double = 0.0     ///  weight in purty for DWT
//    var long_KaratPurity_grams: Double = 0.0   ///  weight in purty for grams
//    var long_KaratPurity_Troy: Double = 0.0    ///  weight in purty for Troy
//
//    var currentMeasureUnit: MeasureUnit = MeasureUnit.DWT
//
//    // result values
//    var Estimated_Value_AfterRefining: Double = 0.0
//    var Total_Weight: Double = 0.0
//    var Display_long_KaratPurity_DWT: Double = 0.0
//    var Display_long_KaratPurity_grams: Double = 0.0
//    var Display_long_KaratPurity_Troy: Double = 0.0
//    var Estimated_Payable_Pure_Gold_Content: Double = 0.0 // FOr  Estimated_Payable_Pure_Gold_Content
//    var Percentage: Double = 0.0
//    var estimatedValue_in_Kformat: Double = 0.0
//
//    // Estimated Refining Terms values
//    var Str_Accountablity: String = "0.0%"
//    var Str_Treatement_Value: String = "$0.0"
//
//    var int_Accountability: Double = 0.0   // change to float
//
//
//    val PurityArray  = listOf(".396", ".479", ".563", ".595", ".729", ".897", ".999")
//    val PurityWeightCalculated = mutableListOf("0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00")
//    val aryTextFieldData = mutableListOf("0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00")
//
//    var internetActive: Boolean = false
//    var isFromButtonClicked: Boolean = false
//    var isSomeDataToCalculate: Boolean = false
//
//
//    /**
//     *  START from KalculatorViewController
//     * */
//    fun MethodUpdateData() {
//        estimatedValue_in_Kformat = 0.0
//
//        Total_Weight = 0.0
//        long_KaratPurity_DWT = 0.0
//        long_KaratPurity_grams = 0.0
//        long_KaratPurity_Troy = 0.0
//
//        for (i in 0..6) {
//            PurityWeightCalculated[i] = String.format(
//                "%.6f",
//                aryTextFieldData[i].toDoubleValue() * PurityArray[i].toDoubleValue()
//            )
//            long_KaratPurity_DWT += PurityWeightCalculated[i].toDoubleValue()
//            long_KaratPurity_grams += PurityWeightCalculated[i].toDoubleValue();
//            long_KaratPurity_Troy += PurityWeightCalculated[i].toDoubleValue();
//            Total_Weight += aryTextFieldData[i].toDoubleValue();
//        }
//
//        if (long_KaratPurity_DWT == 0.0 && long_KaratPurity_grams == 0.0 && long_KaratPurity_Troy == 0.0 && isFromButtonClicked) {
//            isSomeDataToCalculate = false
//            MaterialAlertDialogBuilder(App.getContext())
//                .setTitle("Karat Calculator")
//                .setMessage("There must be at least one quantity to calculate.")
//                .setPositiveButton(android.R.string.ok, null)
//                .show()
//        } else {
//            //long_KaratPurity_DWT
//            long_KaratPurity_DWT /= 20
//            long_KaratPurity_grams /= 31.1035;
//            Total_Weight = RoundedValueForEstimated_Value(Total_Weight)
//
//
//            Display_long_KaratPurity_DWT = long_KaratPurity_DWT
//            Display_long_KaratPurity_grams = long_KaratPurity_grams
//            Display_long_KaratPurity_Troy = long_KaratPurity_Troy
//
//            NSLog("   total weight  == == === =%f", Total_Weight)
//            NSLog(" befor long_KaratPurity_DWT == == === =%f", long_KaratPurity_DWT);
//            NSLog(" before long_KaratPurity_Grams == == === =%f", long_KaratPurity_grams);
//            NSLog(" before  long_KaratPurity_Troy == == === =%f", long_KaratPurity_Troy);
//            long_KaratPurity_DWT = RoundecValue(long_KaratPurity_DWT)
//            long_KaratPurity_grams = RoundecValue(long_KaratPurity_grams)
//            long_KaratPurity_Troy = RoundecValue(long_KaratPurity_Troy)
//            NSLog("long_KaratPurity_DWT == == === =%f", long_KaratPurity_DWT);
//            NSLog("long_KaratPurity_Grams == == === =%f", long_KaratPurity_grams);
//            NSLog("long_KaratPurity_Troy == == === =%f", long_KaratPurity_Troy);
//
//
//            ///  For Final Estimated Value After All Refining Charges-
//
//
//            when (currentMeasureUnit) {
//                MeasureUnit.DWT -> {
//                    Total_Weight /= 20
//
//                    if (long_KaratPurity_DWT > 0 && long_KaratPurity_DWT <= 5.0) {
//                        int_Accountability = .97
//                        Estimated_Value_AfterRefining =
//                            (.97 * long_GoldRate * Display_long_KaratPurity_DWT) - 100;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        NSLog("Estimated_Value_AfterRefining===%.2f", Estimated_Value_AfterRefining);
//                        Str_Treatement_Value = "100.00";
//                        Str_Accountablity = "97";
//                    } else if (long_KaratPurity_DWT > 5.0 && long_KaratPurity_DWT <= 30.0) {
//                        int_Accountability = .98;
//                        Estimated_Value_AfterRefining =
//                            (.98 * long_GoldRate * Display_long_KaratPurity_DWT) - 50;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "50.00";
//                        Str_Accountablity = "98";
//
//
//                    } else if (long_KaratPurity_DWT > 30 && long_KaratPurity_DWT <= 60) {
//                        int_Accountability = .985;
//                        Estimated_Value_AfterRefining =
//                            .985 * long_GoldRate * Display_long_KaratPurity_DWT - 50;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "50.00";
//                        Str_Accountablity = "98.5";
//
//
//                    } else if (long_KaratPurity_DWT > 60 && long_KaratPurity_DWT <= 110) {
//                        int_Accountability = .985;
//                        Estimated_Value_AfterRefining =
//                            .985 * long_GoldRate * Display_long_KaratPurity_DWT;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "Waived";
//                        Str_Accountablity = "98.5";
//
//
//                    } else {
//                        if (long_KaratPurity_DWT == 0.0) {
//                            int_Accountability = 0.0;
//                            Str_Accountablity = "0.0";
//                            Str_Treatement_Value = "0.0";
//                            NSLog("Called in DWTTTTTTTTT");
//                        } else {
//                            int_Accountability = .99;
//                            Estimated_Value_AfterRefining =
//                                (.99 * long_GoldRate * Display_long_KaratPurity_DWT) - (Total_Weight * 0.75);
//                            Estimated_Value_AfterRefining =
//                                RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                            Str_Treatement_Value = String.format("%.2f", Total_Weight * 0.75)
//                            Str_Accountablity = "99";
//                        }
//
//
//                    }
//                    NSLog("est value  from DWT = == = %f", Estimated_Value_AfterRefining);
//
//                    ///// FOr  Estimated_Payable_Pure_Gold_Content
//                    Estimated_Payable_Pure_Gold_Content =
//                        Display_long_KaratPurity_DWT * int_Accountability;
//                    Estimated_Payable_Pure_Gold_Content =
//                        RoundedValueForEstimated_ValueUptoTwo_Value(Estimated_Payable_Pure_Gold_Content)
//                    NSLog(
//                        "est value  from dwt after   = == = %.2f",
//                        Estimated_Payable_Pure_Gold_Content
//                    );
//                    NSLog("str value  dwt=== %f", int_Accountability);
//
//                    //Calculate Percentage;
//                    Percentage = Display_long_KaratPurity_DWT / Total_Weight;
//                    Percentage = Percentage * 100;
//                    Percentage = RoundedValueForEstimated_ValueForPercentage(Percentage)
//                    //Percentage = RoundedValueForEstimated_ValueUptoTwo_Value:Percentage];
//                    NSLog("Percentage  DWT=============>>>>>>>>%.2f", Percentage);
//
//                }
//                MeasureUnit.GRAM -> {
//                    Total_Weight /= 31.1035;
//
//                    if (long_KaratPurity_grams > 0 && long_KaratPurity_grams <= 5.0) {
//                        int_Accountability = .97
//                        Estimated_Value_AfterRefining =
//                            .97 * long_GoldRate * Display_long_KaratPurity_grams - 100;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "100.00";
//                        Str_Accountablity = "97";
//
//                    } else if (long_KaratPurity_grams > 5.0 && long_KaratPurity_grams <= 30.0) {
//                        int_Accountability = .98;
//                        Estimated_Value_AfterRefining =
//                            .98 * long_GoldRate * Display_long_KaratPurity_grams - 50;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "50.00";
//                        Str_Accountablity = "98";
//
//
//                    } else if (long_KaratPurity_grams > 30 && long_KaratPurity_grams <= 60) {
//                        int_Accountability = .985;
//                        Estimated_Value_AfterRefining =
//                            .985 * long_GoldRate * Display_long_KaratPurity_grams - 50;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "50.00";
//                        Str_Accountablity = "98.5";
//
//
//                    } else if (long_KaratPurity_grams > 60 && long_KaratPurity_grams <= 110) {
//                        int_Accountability = .985;
//                        Estimated_Value_AfterRefining =
//                            .985 * long_GoldRate * Display_long_KaratPurity_grams;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "Waived";
//                        Str_Accountablity = "98.5";
//
//
//                    } else {
//                        if (long_KaratPurity_grams == 0.0) {
//                            int_Accountability = 0.0;
//                            Str_Accountablity = "0.0";
//                            Str_Treatement_Value = "0.0";
//                            NSLog("Called in Gramsss");
//
//                        } else {
//                            int_Accountability = .99;
//                            Estimated_Value_AfterRefining =
//                                (.99 * long_GoldRate * Display_long_KaratPurity_grams) - (Total_Weight * 0.75);
//                            Estimated_Value_AfterRefining =
//                                RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                            Str_Treatement_Value = String.format("%.2f", Total_Weight * 0.75)
//                            Str_Accountablity = "99";
//                        }
//
//                    }
//                    NSLog("est value  from grams = == = %f", Estimated_Value_AfterRefining);
//                    ///// FOr  Estimated_Payable_Pure_Gold_Content
//
//                    Estimated_Payable_Pure_Gold_Content =
//                        Display_long_KaratPurity_grams * int_Accountability;
//                    Estimated_Payable_Pure_Gold_Content =
//                        RoundedValueForEstimated_ValueUptoTwo_Value(Estimated_Payable_Pure_Gold_Content)
//
//                    NSLog(
//                        "est value  from grams after   = == = %.2f",
//                        Estimated_Payable_Pure_Gold_Content
//                    );
//
//                    //NSLog("est value  from grams after   = == = %f",Estimated_Payable_Pure_Gold_Content);
//                    NSLog("str value  grams=== %f", int_Accountability);
//
//
//                    //Percentage
//                    Percentage = Display_long_KaratPurity_grams / Total_Weight;
//                    Percentage = Percentage * 100;
//                    Percentage = RoundedValueForEstimated_ValueForPercentage(Percentage)
//                    NSLog("Percentage grams =============>>>>>>>>%f", Percentage);
//                }
//                MeasureUnit.TROY -> {
//                    if (long_KaratPurity_Troy > 0 && long_KaratPurity_Troy <= 5.0) {
//                        int_Accountability = .97;
//                        Estimated_Value_AfterRefining =
//                            .97 * long_GoldRate * Display_long_KaratPurity_Troy - 100;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "100";
//                        Str_Accountablity = "97";
//
//                    } else if (long_KaratPurity_Troy > 5.0 && long_KaratPurity_Troy <= 30.0) {
//                        int_Accountability = .98;
//                        Estimated_Value_AfterRefining =
//                            .98 * long_GoldRate * Display_long_KaratPurity_Troy - 50;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "50";
//                        Str_Accountablity = "98";
//
//
//                    } else if (long_KaratPurity_Troy > 30 && long_KaratPurity_Troy <= 60) {
//                        int_Accountability = .985;
//                        Estimated_Value_AfterRefining =
//                            .985 * long_GoldRate * Display_long_KaratPurity_Troy - 50;
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "50";
//                        Str_Accountablity = "98.5";
//
//
//                    } else if (long_KaratPurity_Troy > 60 && long_KaratPurity_Troy <= 110) {
//                        int_Accountability = .985
//                        Estimated_Value_AfterRefining =
//                            .985 * long_GoldRate * Display_long_KaratPurity_Troy
//                        Estimated_Value_AfterRefining =
//                            RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                        Str_Treatement_Value = "Waived";
//                        Str_Accountablity = "98.5";
//
//
//                    } else {
//                        if (long_KaratPurity_Troy == 0.0) {
//                            int_Accountability = 0.0;
//                            Str_Accountablity = "0.0";
//                            Str_Treatement_Value = "0.0";
//                            NSLog("Called in Troyyyyyyyyy");
//                        } else {
//                            int_Accountability = .99;
//                            Estimated_Value_AfterRefining =
//                                (.99 * long_GoldRate * Display_long_KaratPurity_Troy) - (Total_Weight * 0.75);
//                            Estimated_Value_AfterRefining =
//                                RoundedValueForEstimated_Value(Estimated_Value_AfterRefining)
//                            Str_Treatement_Value = String.format("%.2f", Total_Weight * 0.75)
//                            Str_Accountablity = "99";
//                        }
//
//                    }
//
//                    NSLog("est value  from troz = == = %f", Estimated_Value_AfterRefining);
//                    ///// FOr  Estimated_Payable_Pure_Gold_Content
//
//                    Estimated_Payable_Pure_Gold_Content =
//                        Display_long_KaratPurity_Troy * int_Accountability;
//                    Estimated_Payable_Pure_Gold_Content =
//                        RoundedValueForEstimated_ValueUptoTwo_Value(Estimated_Payable_Pure_Gold_Content)
//                    NSLog(
//                        "est value  from troy after   = == = %f",
//                        Estimated_Payable_Pure_Gold_Content
//                    );
//                    //Calculate Percentage;
//                    Percentage = Display_long_KaratPurity_Troy / Total_Weight
//                    Percentage = Percentage * 100;
//                    Percentage = RoundedValueForEstimated_ValueForPercentage(Percentage)
//                    NSLog("Percentage  Troy=============>>>>>>>>%f", Percentage)
//                }
//            }
//            //estimatedValue_in_Kformat
//            estimatedValue_in_Kformat = (24 * Percentage) / 100
//            estimatedValue_in_Kformat =
//                RoundedValueForEstimated_ValueUptoTwo_Value(estimatedValue_in_Kformat)
//            NSLog("estimated valuein kformat%.2f", estimatedValue_in_Kformat)
//            NSLog("str value  grams=== %f", int_Accountability)
//            isSomeDataToCalculate = TRUE
//        }
//    }

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

        totalWeight = RoundedValueForEstimated_Value(totalWeight)

        NSLog("   total weight  == == === =%f", totalWeight)
        NSLog(" before long_KaratPurity_DWT == == === =%f", karatPurityDWT)
        NSLog(" before long_KaratPurity_Grams == == === =%f", karatPurityGrams)
        NSLog(" before long_KaratPurity_Troy == == === =%f", karatPurityTroy)

        val roundedPurityDWT = RoundecValue(karatPurityDWT)
        val roundedPurityGrams = RoundecValue(karatPurityGrams)
        val roundedPurityTroy = RoundecValue(karatPurityTroy)

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
                        RoundedValueForEstimated_Value(finalEstimate)
                    NSLog("Estimated_Value_AfterRefining===%.2f", finalEstimate)
                    treatment = 100.0
                } else if (roundedPurityDWT > 5.0 && roundedPurityDWT <= 30.0) {
                    accountability = 98.0
                    finalEstimate = (.98 * goldPrice * karatPurityDWT) - 50
                    finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityDWT > 30.0 && roundedPurityDWT <= 60.0) {
                    accountability = 98.5
                    finalEstimate =
                        .985 * goldPrice * karatPurityDWT - 50
                    finalEstimate =
                        RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityDWT > 60 && roundedPurityDWT <= 110) {
                    accountability = 98.5
                    finalEstimate =
                        .985 * goldPrice * karatPurityDWT
                    finalEstimate =
                        RoundedValueForEstimated_Value(finalEstimate)
                    treatment = -1.0 //"Waived"


                } else {
                    if (roundedPurityDWT == 0.0) {
                        accountability = 0.0
                        treatment = 0.0
                        NSLog("Called in DWTTTTTTTTT")
                    } else {
                        accountability = 99.0
                        finalEstimate = (.99 * goldPrice * karatPurityDWT) - (totalWeight * 0.75)
                        finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                        treatment = RoundecValue(totalWeight * 0.75)
                    }


                }
                NSLog("est value  from DWT = == = %f", finalEstimate)

                ///// FOr  Estimated_Payable_Pure_Gold_Content
                estPayablePure = karatPurityDWT * accountability / 100.0
                estPayablePure = RoundedValueForEstimated_ValueUptoTwo_Value(estPayablePure)
                NSLog("est value  from dwt after   = == = %.2f", estPayablePure)
                NSLog("str value  dwt=== %f", accountability)

                //Calculate Percentage
                estPercentage = karatPurityDWT / totalWeight
                estPercentage *= 100
                estPercentage = RoundedValueForEstimated_ValueForPercentage(estPercentage)
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
                        RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 100.0

                } else if (roundedPurityGrams > 5.0 && roundedPurityGrams <= 30.0) {
                    accountability = 98.0
                    finalEstimate =
                        .98 * goldPrice * karatPurityGrams - 50
                    finalEstimate =
                        RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 50.0

                } else if (roundedPurityGrams > 30 && roundedPurityGrams <= 60) {
                    accountability = 98.5
                    finalEstimate = 0.985 * goldPrice * karatPurityGrams - 50
                    finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 50.00

                } else if (roundedPurityGrams > 60 && roundedPurityGrams <= 110) {
                    accountability = 98.5
                    finalEstimate = 0.985 * goldPrice * karatPurityGrams
                    finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                    treatment = -1.0 //"Waived"

                } else {
                    if (roundedPurityGrams == 0.0) {
                        accountability = 0.0
                        treatment = 0.0
                        NSLog("Called in Gramsss")

                    } else {
                        accountability = 99.0
                        finalEstimate = (0.99 * goldPrice * karatPurityGrams) - (totalWeight * 0.75)
                        finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                        treatment = RoundecValue(totalWeight * 0.75)
                    }

                }
                NSLog("est value  from grams = == = %f", finalEstimate)
                ///// FOr  Estimated_Payable_Pure_Gold_Content

                estPayablePure = karatPurityGrams * accountability / 100.0
                estPayablePure = RoundedValueForEstimated_ValueUptoTwo_Value(estPayablePure)

                NSLog("est value  from grams after   = == = %.2f", estPayablePure)

                //NSLog("est value  from grams after   = == = %f",Estimated_Payable_Pure_Gold_Content)
                NSLog("str value  grams=== %f", accountability)

                //Percentage
                estPercentage = karatPurityGrams / totalWeight
                estPercentage *= 100.0
                estPercentage = RoundedValueForEstimated_ValueForPercentage(estPercentage)
                NSLog("Percentage grams =============>>>>>>>>%f", estPercentage)
            }
            MeasureUnit.TROY -> {
                displayKaratPurity = karatPurityTroy

                if (roundedPurityTroy > 0 && roundedPurityTroy <= 5.0) {
                    accountability = 97.0
                    finalEstimate = .97 * goldPrice * karatPurityTroy - 100
                    finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 100.0

                } else if (roundedPurityTroy > 5.0 && roundedPurityTroy <= 30.0) {
                    accountability = 98.0
                    finalEstimate = .98 * goldPrice * karatPurityTroy - 50
                    finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityTroy > 30 && roundedPurityTroy <= 60) {
                    accountability = 98.5
                    finalEstimate = .985 * goldPrice * karatPurityTroy - 50
                    finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                    treatment = 50.0


                } else if (roundedPurityTroy > 60 && roundedPurityTroy <= 110) {
                    accountability = 98.5
                    finalEstimate = .985 * goldPrice * karatPurityTroy
                    finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                    treatment = -1.0 // "Waived"


                } else {
                    if (roundedPurityTroy == 0.0) {
                        accountability = 0.0
                        treatment = 0.0
                        NSLog("Called in Troyyyyyyyyy")
                    } else {
                        accountability = 99.0
                        finalEstimate = (.99 * goldPrice * karatPurityTroy) - (totalWeight * 0.75)
                        finalEstimate = RoundedValueForEstimated_Value(finalEstimate)
                        treatment = RoundecValue(totalWeight * 0.75)
                    }

                }

                NSLog("est value  from troz = == = %f", finalEstimate)
                ///// FOr  Estimated_Payable_Pure_Gold_Content

                estPayablePure = karatPurityTroy * accountability / 100.0
                estPayablePure = RoundedValueForEstimated_ValueUptoTwo_Value(estPayablePure)
                NSLog("est value  from troy after   = == = %f", estPayablePure)

                //Calculate Percentage
                estPercentage = karatPurityTroy / totalWeight
                estPercentage *= 100.0
                estPercentage = RoundedValueForEstimated_ValueForPercentage(estPercentage)
                NSLog("Percentage  Troy=============>>>>>>>>%f", estPercentage)
            }
        }

        //estimatedValue_in_Kformat
        var estKarat = (24.0 * estPercentage) / 100.0
        estKarat = RoundedValueForEstimated_ValueUptoTwo_Value(estKarat)
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

    fun roundedValue(FloatValue: Double, decimalCount: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.minimumFractionDigits = decimalCount
        numberFormat.maximumFractionDigits = decimalCount
        numberFormat.roundingMode = RoundingMode.HALF_UP
        //[numberFormat setNumberStyle:NSNumberFormatterDecimalStyle];
        return numberFormat.format(FloatValue)
    }

    fun roundedShort(FloatValue: Double, decimalCount: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = decimalCount
        numberFormat.roundingMode = RoundingMode.HALF_UP
        //[numberFormat setNumberStyle:NSNumberFormatterDecimalStyle];
        return numberFormat.format(FloatValue)
    }


    fun RoundecValue(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    fun RoundedValueForEstimated_Value(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 3
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    fun RoundedValueForEstimated_ValueUptoTwo_Value(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 3
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    fun RoundedValueForEstimated_ValueForPercentage(FloatValue: Double): Double {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = 4
        numberFormat.roundingMode = RoundingMode.HALF_UP
        return numberFormat.format(FloatValue).toDoubleValue()
    }

    fun displayDecimalText(decimal: Double): String {
        // ex: 24234.20
        val df = DecimalFormat("0.00")
        return df.format(decimal)
    }

    /**
     *  END from KalculatorViewController
     * */
}
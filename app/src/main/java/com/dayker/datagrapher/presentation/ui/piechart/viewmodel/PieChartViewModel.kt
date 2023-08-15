package com.dayker.datagrapher.presentation.ui.piechart.viewmodel

import android.graphics.Color
import android.graphics.Typeface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dayker.datagrapher.data.storage.models.PieChartAppearance
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PieChartViewModel @Inject constructor() : ViewModel() {

    private val _pieDataSet = MutableLiveData<PieDataSet>()
    val pieDataSet: LiveData<PieDataSet> = _pieDataSet

    private val _pieChartAppearance = MutableLiveData<PieChartAppearance>()
    val pieChartAppearance: LiveData<PieChartAppearance> = _pieChartAppearance

    private val _pieChartLegend = MutableLiveData<Legend>()
    val pieChartLegend: LiveData<Legend> = _pieChartLegend

    init {
        // Test Data Set
        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(60.5f, "Green"))
        entries.add(PieEntry(26.7f, "Yellow"))
        entries.add(PieEntry(24.0f, "Red"))
        entries.add(PieEntry(140.8f, "Blue"))
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toMutableList()
        dataSet.valueTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        dataSet.colors =
            mutableListOf(
                Color.rgb(1, 255, 1),
                Color.rgb(255, 255, 1),
                Color.rgb(255, 1, 1),
                Color.rgb(1, 1, 255)
            )
        dataSet.valueTextSize = 20f
        dataSet.valueTextColor = Color.WHITE
        _pieDataSet.value = dataSet
        _pieChartAppearance.value = PieChartAppearance()
        val legend = Legend()
        legend.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        legend.isWordWrapEnabled = false
        legend.formToTextSpace = 8f
        legend.textSize = 16f
        _pieChartLegend.value = legend
    }

    fun setFormatter(formatter: PercentFormatter) {
        _pieDataSet.updateValue {
            it.valueFormatter = formatter
        }
    }

    private fun <T> MutableLiveData<T>.updateValue(updateFunction: (T) -> Unit) {
        value?.let { currentValue ->
            updateFunction(currentValue)
            postValue(currentValue)
        }
    }

    fun changeSliceSpace(space: Float) {
        _pieDataSet.updateValue {
            it.sliceSpace = space
        }
    }

    fun changeMaxAngle(angle: Float) {
        _pieChartAppearance.updateValue {
            it.maxAngle = angle
        }
    }

    fun changeHoleRadius(radius: Float) {
        _pieChartAppearance.updateValue {
            it.holeRadius = radius
        }
    }

    fun changeValueTextSize(size: Float) {
        _pieDataSet.updateValue {
            it.valueTextSize = size
        }
    }

    fun changeLabelTextSize(size: Float) {
        _pieChartAppearance.updateValue {
            it.entryLabelTextSize = size
        }
    }

    fun changeTransparentCircleSize(size: Float) {
        _pieChartAppearance.updateValue {
            it.transparentCircleRadius = size
        }
    }

    fun changeTransparentCircleVisibility(alpha: Int) {
        _pieChartAppearance.updateValue {
            it.transparentCircleAlpha = alpha
        }
    }

    fun changeCenterTextSize(size: Float) {
        _pieChartAppearance.updateValue {
            it.centerTextSize = size
        }
    }

    fun changeCenterTextRadius(radius: Float) {
        _pieChartAppearance.updateValue {
            it.centerTextRadius = radius
        }
    }

    fun changeLegendSize(size: Float) {
        _pieChartLegend.updateValue {
            it.formSize = size
        }
    }

    fun changeLegendTextSize(size: Float) {
        _pieChartLegend.updateValue {
            it.textSize = size
        }
    }

    fun changeOffsetFromForm(space: Float) {
        _pieChartLegend.updateValue {
            it.formToTextSpace = space
        }
    }

    fun changeOffsetLeft(value: Float) {
        _pieChartAppearance.updateValue {
            it.extraOffsetLeft = value
        }
    }

    fun changeOffsetRight(value: Float) {
        _pieChartAppearance.updateValue {
            it.extraOffsetRight = value
        }
    }

    fun changeOffsetTop(value: Float) {
        _pieChartAppearance.updateValue {
            it.extraOffsetTop = value
        }
    }

    fun changeOffsetBottom(value: Float) {
        _pieChartAppearance.updateValue {
            it.extraOffsetBottom = value
        }
    }

    fun changeBackgroundColor(hex: String) {
        _pieChartAppearance.updateValue {
            it.backgroundColor = Color.parseColor("#$hex")
        }
    }

    fun changeHoleColor(hex: String) {
        _pieChartAppearance.updateValue {
            it.holeColor = Color.parseColor("#$hex")
        }
    }

    fun changeValueTextColor(hex: String) {
        _pieDataSet.updateValue {
            it.valueTextColor = Color.parseColor("#$hex")
        }
    }

    fun changeLabelTextColor(hex: String) {
        _pieChartAppearance.updateValue {
            it.entryLabelColor = Color.parseColor("#$hex")
        }
    }

    fun changeCircleColor(hex: String) {
        _pieChartAppearance.updateValue {
            it.transparentCircleColor = Color.parseColor("#$hex")
        }
    }

    fun changeCenterTextColor(hex: String) {
        _pieChartAppearance.updateValue {
            it.centerTextColor = Color.parseColor("#$hex")
        }
    }

    fun changeLegendTextColor(hex: String) {
        _pieChartLegend.updateValue {
            it.textColor = Color.parseColor("#$hex")
        }
    }

    fun changeUsePercentages(value: Boolean) {
        _pieChartAppearance.updateValue {
            it.usePercentValues = value
        }
    }

    fun changeValuesTypeface(typeface: Typeface) {
        _pieDataSet.updateValue {
            it.valueTypeface = typeface
        }
    }

    fun changeDisplayLabels(value: Boolean) {
        _pieChartAppearance.updateValue {
            it.setDrawEntryLabels = value
        }
    }

    fun changeLabelsTypeface(typeface: Typeface) {
        _pieChartAppearance.updateValue {
            it.entryLabelTypeface = typeface
        }
    }

    fun changeShowCenterText(value: Boolean) {
        _pieChartAppearance.updateValue {
            it.showCenterText = value
        }
    }

    fun changeCenterTextTypeface(typeface: Typeface) {
        _pieChartAppearance.updateValue {
            it.centerTextTypeface = typeface
        }
    }

    fun changeShowLegend(value: Boolean) {
        _pieChartLegend.updateValue {
            it.isEnabled = value
        }
    }

    fun changeLegendTypeface(typeface: Typeface) {
        _pieChartLegend.updateValue {
            it.typeface = typeface
        }
    }

    fun changeLegendOrientation(orientation: LegendOrientation) {
        _pieChartLegend.updateValue {
            it.orientation = orientation
        }
    }

    fun changeLegendForm(form: Legend.LegendForm) {
        _pieChartLegend.updateValue {
            it.form = form
        }
    }

    fun changeLegendHorizontal(place: Legend.LegendHorizontalAlignment) {
        _pieChartLegend.updateValue {
            it.horizontalAlignment = place
        }
    }

    fun changeLegendVertical(place: Legend.LegendVerticalAlignment) {
        _pieChartLegend.updateValue {
            it.verticalAlignment = place
        }
    }

    fun changeCentralText(text: String) {
        _pieChartAppearance.updateValue {
            it.centerText = text
        }
    }
}
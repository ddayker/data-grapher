package com.dayker.datagrapher.presentation.ui.piechart.viewmodel

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dayker.datagrapher.domain.models.PieChartAppearance
import com.dayker.datagrapher.domain.usecase.CreatePieChartUseCase
import com.dayker.datagrapher.domain.usecase.SaveChartAsImageUseCase
import com.dayker.datagrapher.presentation.ui.piechart.models.PieChartValue
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PieChartViewModel @Inject constructor(
    private val createPieChartUseCase: CreatePieChartUseCase,
    private val saveChartAsImageUseCase: SaveChartAsImageUseCase
) : ViewModel() {

    private val _pieDataSet = MutableLiveData<PieDataSet>()
    val pieDataSet: LiveData<PieDataSet> = _pieDataSet

    private val _pieChartAppearance = MutableLiveData<PieChartAppearance>()
    val pieChartAppearance: LiveData<PieChartAppearance> = _pieChartAppearance

    private val _pieChartLegend = MutableLiveData<Legend>()
    val pieChartLegend: LiveData<Legend> = _pieChartLegend

    val pieChartValues: LiveData<List<PieChartValue>> = pieDataSet.map { mapToPieChartValueList() }

    init {
        initPieChart()
    }

    fun initPieChart(){
        val pieChart = createPieChartUseCase.execute()
        _pieChartAppearance.value = pieChart.appearance
        _pieDataSet.value = pieChart.dataSet
        _pieChartLegend.value = pieChart.legend
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

    private fun mapToPieChartValueList(): List<PieChartValue> {
        val pieChartValues = mutableListOf<PieChartValue>()
        val pieDataSet = _pieDataSet.value
        pieDataSet?.let {
            for (i in 0 until it.entryCount) {
                val entry = it.getEntryForIndex(i)
                pieChartValues.add(PieChartValue(entry.value, entry.label, it.colors[i]))
            }
        }
        return pieChartValues
    }

    fun addEntry(value: Float, label: String, color: Int) {
        _pieDataSet.updateValue {
            it.addEntry(PieEntry(value, label))
            it.colors?.add(color)
        }
    }

    fun updateEntry(index: Int, value: Float, label: String, color: Int) {
        _pieDataSet.updateValue {
            it.values[index] = PieEntry(value, label)
            it.colors[index] = color
        }
    }

    fun deleteEntry(index: Int) {
        _pieDataSet.updateValue {
            it.removeEntry(index)
            it.colors.removeAt(index)
        }
    }

    fun setChartName(name: String){
        _pieDataSet.updateValue {
            it.label = name
        }
    }

    fun saveChartToGallery(bitmap: Bitmap, name: String): Boolean {
        return saveChartAsImageUseCase.execute(bitmap = bitmap, name = name)
    }


}
package com.dayker.datagrapher.domain.usecase

import android.graphics.Typeface
import com.dayker.datagrapher.domain.models.PieChartAppearance
import com.dayker.datagrapher.domain.models.PieChartEntity
import com.dayker.datagrapher.utils.Constants.CHART_DEFAULT_LABEL
import com.dayker.datagrapher.utils.Constants.VALUE_FOUR_LABEL
import com.dayker.datagrapher.utils.Constants.VALUE_ONE_LABEL
import com.dayker.datagrapher.utils.Constants.VALUE_THREE_LABEL
import com.dayker.datagrapher.utils.Constants.VALUE_TWO_LABEL
import com.dayker.datagrapher.utils.PieChartDefaults.LEGEND_FORM_TO_TEXT_SPACE
import com.dayker.datagrapher.utils.PieChartDefaults.LEGEND_TEXT_SIZE
import com.dayker.datagrapher.utils.PieChartDefaults.VALUE_FOUR
import com.dayker.datagrapher.utils.PieChartDefaults.VALUE_ONE
import com.dayker.datagrapher.utils.PieChartDefaults.VALUE_TEXT_COLOR
import com.dayker.datagrapher.utils.PieChartDefaults.VALUE_TEXT_SIZE
import com.dayker.datagrapher.utils.PieChartDefaults.VALUE_THREE
import com.dayker.datagrapher.utils.PieChartDefaults.VALUE_TWO
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class CreatePieChartUseCase() {

    fun execute(): PieChartEntity {
        return PieChartEntity(
            appearance = setUpAppearance(),
            dataSet = setUpDataSet(),
            legend = setUpPieChartLegend()
        )
    }

    private fun setUpAppearance(): PieChartAppearance {
        return PieChartAppearance()
    }

    private fun setUpDataSet(): PieDataSet {
        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(VALUE_ONE, VALUE_ONE_LABEL))
        entries.add(PieEntry(VALUE_TWO, VALUE_TWO_LABEL))
        entries.add(PieEntry(VALUE_THREE, VALUE_THREE_LABEL))
        entries.add(PieEntry(VALUE_FOUR, VALUE_FOUR_LABEL))
        val dataSet = PieDataSet(entries, CHART_DEFAULT_LABEL)
        dataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toMutableList().take(dataSet.entryCount)
        dataSet.valueTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        dataSet.valueTextSize = VALUE_TEXT_SIZE
        dataSet.valueTextColor = VALUE_TEXT_COLOR
        return dataSet
    }

    private fun setUpPieChartLegend(): Legend {
        val legend = Legend()
        legend.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        legend.formToTextSpace = LEGEND_FORM_TO_TEXT_SPACE
        legend.textSize = LEGEND_TEXT_SIZE
        return legend
    }

}
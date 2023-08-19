package com.dayker.datagrapher.presentation.ui.piechart.models

data class PieChartValue(var value: Float, var label: String, var color: Int) {
    companion object {
        var totalAmount: Float = 0f
    }
}
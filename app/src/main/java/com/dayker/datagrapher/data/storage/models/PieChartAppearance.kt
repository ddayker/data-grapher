package com.dayker.datagrapher.data.storage.models

import android.graphics.Typeface
import com.dayker.datagrapher.utils.PieChartDefaults

data class PieChartAppearance(

    var backgroundColor: Int = PieChartDefaults.BACKGROUND_COLOR,

    var maxAngle: Float = PieChartDefaults.MAX_ANGLE,

    var usePercentValues: Boolean = PieChartDefaults.USE_PERCENT_VALUES,

    var setDrawEntryLabels: Boolean = PieChartDefaults.SET_DRAW_ENTRY_LABELS,

    var entryLabelColor: Int = PieChartDefaults.ENTRY_LABEL_COLOR,

    var entryLabelTextSize: Float = PieChartDefaults.ENTRY_LABEL_TEXT_SIZE,

    var entryLabelTypeface: Typeface = Typeface.DEFAULT,

    var extraOffsetLeft: Float = PieChartDefaults.EXTRA_OFFSET_LEFT,

    var extraOffsetRight: Float = PieChartDefaults.EXTRA_OFFSET_RIGHT,

    var extraOffsetTop: Float = PieChartDefaults.EXTRA_OFFSET_TOP,

    var extraOffsetBottom: Float = PieChartDefaults.EXTRA_OFFSET_BOTTOM,

    var showCenterText: Boolean = PieChartDefaults.SHOW_CENTER_TEXT,

    var centerText: String = PieChartDefaults.CENTER_TEXT,

    var centerTextRadius: Float = PieChartDefaults.CENTER_TEXT_RADIUS,

    var centerTextColor: Int = PieChartDefaults.CENTER_TEXT_COLOR,

    var centerTextSize: Float = PieChartDefaults.CENTER_TEXT_SIZE,

    var centerTextTypeface: Typeface = Typeface.DEFAULT,

    var holeRadius: Float = PieChartDefaults.HOLE_RADIUS,

    var holeColor: Int = PieChartDefaults.HOLE_COLOR,

    var transparentCircleRadius: Float = PieChartDefaults.TRANSPARENT_CIRCLE_RADIUS,

    var transparentCircleAlpha: Int = PieChartDefaults.TRANSPARENT_CIRCLE_ALPHA,

    var transparentCircleColor: Int = PieChartDefaults.TRANSPARENT_CIRCLE_COLOR,
)



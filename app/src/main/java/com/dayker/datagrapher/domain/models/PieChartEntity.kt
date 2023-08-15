package com.dayker.datagrapher.domain.models

import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieDataSet

class PieChartEntity(val appearance: PieChartAppearance,val dataSet: PieDataSet,val legend: Legend)
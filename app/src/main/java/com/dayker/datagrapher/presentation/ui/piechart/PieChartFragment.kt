package com.dayker.datagrapher.presentation.ui.piechart

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.FragmentPieChartBinding
import com.dayker.datagrapher.domain.models.PieChartAppearance
import com.dayker.datagrapher.presentation.core.ChartExporter
import com.dayker.datagrapher.presentation.ui.piechart.viewmodel.PieChartViewModel
import com.dayker.datagrapher.utils.PieChartDefaults.ANIMATION_TIME
import com.dayker.datagrapher.utils.UIUtilities.showConfirmationDialog
import com.dayker.datagrapher.utils.UIUtilities.showSaveConfirmationDialog
import com.dayker.datagrapher.utils.Utilities.executeIfExternalStoragePermissionGranted
import com.dayker.datagrapher.utils.Utilities.generateImageName
import com.dayker.datagrapher.utils.Utilities.viewToBitmap
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PieChartFragment : Fragment(), ChartExporter {

    private var binding: FragmentPieChartBinding? = null
    private val viewModel: PieChartViewModel by activityViewModels()
    private lateinit var currentAppearance: PieChartAppearance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPieChartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentAppearance = PieChartAppearance()
        viewModel.setFormatter(PercentFormatter(binding?.chart))
        setGeneralSettings()
        setUpToolBar()

        viewModel.pieDataSet.observe(this) { pieDataSet ->
            recreatePieChart(PieData(pieDataSet))
        }
        viewModel.pieChartAppearance.observe(this) { appearance ->
            updateChartAppearance(appearance)
            currentAppearance = appearance.copy()
        }
        viewModel.pieChartLegend.observe(this) { legend ->
            updateLegend(legend)
        }

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.container_pie_chart) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.piechart_nav_graph)

        binding?.bottomSheet?.radioValues?.setOnClickListener {
            if (navController.currentDestination?.id == R.id.pieChartAppearanceFragment) {
                navController.navigate(R.id.action_pieChartAppearanceFragment_to_pieChartValuesFragment)
            }
        }

        binding?.bottomSheet?.radioAppearance?.setOnClickListener {
            if (navController.currentDestination?.id == R.id.pieChartValuesFragment) {
                navController.navigate(R.id.action_pieChartValuesFragment_to_pieChartAppearanceFragment)
            }
        }
    }

    private inline fun updateFieldIfNeeded(
        currentValue: Any,
        newValue: Any,
        updateFunction: (Any) -> Unit
    ) {
        if (currentValue != newValue) {
            updateFunction(newValue)
        }
    }

    private fun updateChartAppearance(appearance: PieChartAppearance) {
        val chart = binding?.chart ?: return
        with(chart) {
            updateFieldIfNeeded(currentAppearance.backgroundColor, appearance.backgroundColor) { setBackgroundColor(it as Int) }
            updateFieldIfNeeded(currentAppearance.maxAngle, appearance.maxAngle) { maxAngle = it as Float }
            updateFieldIfNeeded(currentAppearance.usePercentValues, appearance.usePercentValues) { setUsePercentValues(it as Boolean) }
            updateFieldIfNeeded(currentAppearance.setDrawEntryLabels, appearance.setDrawEntryLabels) { setDrawEntryLabels(it as Boolean) }
            updateFieldIfNeeded(currentAppearance.entryLabelColor, appearance.entryLabelColor) { setEntryLabelColor(it as Int) }
            updateFieldIfNeeded(currentAppearance.entryLabelTextSize, appearance.entryLabelTextSize) { setEntryLabelTextSize(it as Float) }
            updateFieldIfNeeded(currentAppearance.entryLabelTypeface, appearance.entryLabelTypeface) { setEntryLabelTypeface(it as Typeface) }
            updateFieldIfNeeded(currentAppearance.extraOffsetTop, appearance.extraOffsetTop) { extraTopOffset = it as Float }
            updateFieldIfNeeded(currentAppearance.extraOffsetBottom, appearance.extraOffsetBottom) { extraBottomOffset = it as Float }
            updateFieldIfNeeded(currentAppearance.extraOffsetLeft, appearance.extraOffsetLeft) { extraLeftOffset = it as Float }
            updateFieldIfNeeded(currentAppearance.extraOffsetRight, appearance.extraOffsetRight) { extraRightOffset = it as Float }
            updateFieldIfNeeded(currentAppearance.centerText, appearance.centerText) { centerText = it as String }
            updateFieldIfNeeded(currentAppearance.centerTextRadius, appearance.centerTextRadius) { centerTextRadiusPercent = it as Float }
            updateFieldIfNeeded(currentAppearance.centerTextColor, appearance.centerTextColor) { setCenterTextColor(it as Int) }
            updateFieldIfNeeded(currentAppearance.centerTextSize, appearance.centerTextSize) { setCenterTextSize(it as Float) }
            updateFieldIfNeeded(currentAppearance.centerTextTypeface, appearance.centerTextTypeface) { setCenterTextTypeface(it as Typeface) }
            updateFieldIfNeeded(currentAppearance.holeRadius, appearance.holeRadius) { holeRadius = it as Float }
            updateFieldIfNeeded(currentAppearance.holeColor, appearance.holeColor) { setHoleColor(it as Int) }
            updateFieldIfNeeded(currentAppearance.transparentCircleRadius, appearance.transparentCircleRadius) { transparentCircleRadius = it as Float }
            updateFieldIfNeeded(currentAppearance.transparentCircleAlpha, appearance.transparentCircleAlpha) { setTransparentCircleAlpha(it as Int) }
            updateFieldIfNeeded(currentAppearance.transparentCircleColor, appearance.transparentCircleColor) { setTransparentCircleColor(it as Int) }
            recreatePieChart(data)
        }
    }

    private inline fun <T> Any?.updatePropertyIfChanged(newValue: T, updateFunction: (T) -> Unit) {
        if (this != null && this != newValue) {
            updateFunction(newValue)
        }
    }

    private fun updateLegend(legend: Legend) {
        binding?.chart?.legend?.let { chartLegend ->
            with(chartLegend) {
                updatePropertyIfChanged(legend.isEnabled) { isEnabled = it }
                updatePropertyIfChanged(legend.orientation) { orientation = it }
                updatePropertyIfChanged(legend.form) { form = it }
                updatePropertyIfChanged(legend.textSize) { textSize = Utils.convertPixelsToDp(it) }
                updatePropertyIfChanged(legend.textColor) { textColor = it }
                updatePropertyIfChanged(legend.typeface) { typeface = it }
                updatePropertyIfChanged(legend.formSize) { formSize = it }
                updatePropertyIfChanged(legend.verticalAlignment) { verticalAlignment = it }
                updatePropertyIfChanged(legend.horizontalAlignment) { horizontalAlignment = it }
                updatePropertyIfChanged(legend.formToTextSpace) { formToTextSpace = it }
            }
            binding?.chart?.invalidate()
        }
    }

    private fun recreatePieChart(pieData: PieData?) {
        binding?.chart?.data = pieData
        binding?.chart?.invalidate()
    }

    private fun setUpToolBar() {
        binding?.appBar?.toolBarCreation?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding?.appBar?.btnReset?.setOnClickListener {
            showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.reset_chart),
                message = getString(R.string.lost_chart_warning)
            ) { viewModel.initPieChart() }
        }
        binding?.appBar?.btnExport?.setOnClickListener {
            exportChartImage()
        }
    }

     override fun exportChartImage(){
        val bitmap = viewToBitmap(view = binding?.chart!!)
        val displayName = generateImageName(requireContext())
        if (Build.VERSION.SDK_INT < 29) {
            executeIfExternalStoragePermissionGranted(
                context = requireContext(),
                activity = requireActivity()
            ) {
                showSaveConfirmationDialog(name = displayName, context = requireContext(), snackBarView = binding?.root!!){
                    viewModel.saveChartToGallery(bitmap = bitmap, name = it)
                }
            }
        } else {
            showSaveConfirmationDialog(name = displayName, context = requireContext(), snackBarView = binding?.root!!){
                viewModel.saveChartToGallery(bitmap = bitmap, name = it)
            }
        }
    }


    /**
     * Applies general settings to the all pie charts.
     * These settings cannot be configured by the user.
     */
    private fun setGeneralSettings() {
        // Animate the chart with a bounce easing effect
        binding?.chart?.animateY(ANIMATION_TIME, Easing.EaseOutBounce)
        // Disable the chart description
        binding?.chart?.description?.isEnabled = false
        // Set the use of percent values for chart entries.
        binding?.chart?.setUsePercentValues(true)
    }
}
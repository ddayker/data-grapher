package com.dayker.datagrapher.presentation.ui.piechart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dayker.datagrapher.R
import com.dayker.datagrapher.domain.models.PieChartAppearance
import com.dayker.datagrapher.databinding.FragmentPieChartAppearanceBinding
import com.dayker.datagrapher.presentation.ui.piechart.viewmodel.PieChartViewModel
import com.dayker.datagrapher.utils.PieChartDefaults.MIN_ANGLE
import com.dayker.datagrapher.utils.PieChartDefaults.MIN_CENTER_TEXT_SIZE
import com.dayker.datagrapher.utils.PieChartDefaults.MIN_LABEL_TEXT_SIZE
import com.dayker.datagrapher.utils.PieChartDefaults.MIN_LEGEND_TEXT_SIZE
import com.dayker.datagrapher.utils.PieChartDefaults.MIN_VALUE_TEXT_SIZE
import com.dayker.datagrapher.utils.Utils.isBold
import com.dayker.datagrapher.utils.Utils.isItalic
import com.dayker.datagrapher.utils.Utils.observeOnce
import com.dayker.datagrapher.utils.Utils.onProgressChanged
import com.dayker.datagrapher.utils.Utils.setupSeekBar
import com.dayker.datagrapher.utils.Utils.showColorPickerDialog
import com.dayker.datagrapher.utils.Utils.showEditTextDialog
import com.dayker.datagrapher.utils.Utils.updateTypeface
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PieChartAppearanceFragment : Fragment() {

    private var binding: FragmentPieChartAppearanceBinding? = null
    private val viewModel: PieChartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPieChartAppearanceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}

        setupSeekBars()
        setupColorPickers()
        setupSwitches()
        setupRadioGroups()
        setupCenterTextEditing()

        viewModel.pieChartAppearance.observeOnce(this) { appearance ->
            initAppearanceSeekBars(appearance)
        }
        viewModel.pieDataSet.observeOnce(this) { dataSet ->
            initDataSeekBars(dataSet)
        }
        viewModel.pieChartLegend.observeOnce(this) { legend ->
            initLegendSeekBars(legend)
        }
        viewModel.pieDataSet.observe(this@PieChartAppearanceFragment) { pieDataSet ->
            updateDataUI(pieDataSet)
        }
        viewModel.pieChartAppearance.observe(this@PieChartAppearanceFragment) { appearance ->
            updateAppearanceUI(appearance)
        }
        viewModel.pieChartLegend.observe(this@PieChartAppearanceFragment) { legend ->
            updateLegendUI(legend)
        }
    }

    private fun setupSeekBars() {
        with(binding) {
            if (this != null) {
                setupSeekBar(seekBarSliceSpace) { viewModel.changeSliceSpace(it) }
                setupSeekBar(seekBarMaxAngle, MIN_ANGLE) { viewModel.changeMaxAngle(it) }
                setupSeekBar(seekBarHoleRadius) { viewModel.changeHoleRadius(it) }
                setupSeekBar(seekBarValueTextSize, MIN_VALUE_TEXT_SIZE) { viewModel.changeValueTextSize(it) }
                setupSeekBar(seekBarLabelTextSize, MIN_LABEL_TEXT_SIZE) { viewModel.changeLabelTextSize(it) }
                setupSeekBar(seekBarCenterTextSize, MIN_CENTER_TEXT_SIZE) { viewModel.changeCenterTextSize(it) }
                setupSeekBar(seekBarCenterTextRadius) { viewModel.changeCenterTextRadius(it) }
                setupSeekBar(seekBarLegendSize) { viewModel.changeLegendSize(it) }
                setupSeekBar(seekBarLegendTextSize, MIN_LEGEND_TEXT_SIZE) { viewModel.changeLegendTextSize(it) }
                setupSeekBar(seekBarFromForm) { viewModel.changeOffsetFromForm(it) }
                setupSeekBar(seekBarOffsetLeft) { viewModel.changeOffsetLeft(it) }
                setupSeekBar(seekBarOffsetRight) { viewModel.changeOffsetRight(it) }
                setupSeekBar(seekBarOffsetTop) { viewModel.changeOffsetTop(it) }
                setupSeekBar(seekBarOffsetBottom) { viewModel.changeOffsetBottom(it) }
                setupSeekBar(seekBarTransparentCircle) { viewModel.changeTransparentCircleSize(it) }
                setupSeekBar(seekBarTransparentCircleVisibility) { viewModel.changeTransparentCircleVisibility(it.toInt()) }
                seekBarHorizontalPosition.onProgressChanged { progress, _ ->
                    when (progress) {
                        0 -> viewModel.changeLegendHorizontal(Legend.LegendHorizontalAlignment.LEFT)
                        1 -> viewModel.changeLegendHorizontal(Legend.LegendHorizontalAlignment.CENTER)
                        2 -> viewModel.changeLegendHorizontal(Legend.LegendHorizontalAlignment.RIGHT)
                    }
                }
                seekBarVerticalPosition.onProgressChanged { progress, _ ->
                    when (progress) {
                        0 -> viewModel.changeLegendVertical(Legend.LegendVerticalAlignment.BOTTOM)
                        1 -> viewModel.changeLegendVertical(Legend.LegendVerticalAlignment.CENTER)
                        2 -> viewModel.changeLegendVertical(Legend.LegendVerticalAlignment.TOP)
                    }
                }
            }
        }
    }

    private fun setupColorPickers() {
        with(binding) {
            if (this != null) {
                backgroundColorPicker.setOnClickListener {
                    showColorPickerDialog(
                        requireContext(),
                        getString(R.string.background_color_pick)
                    ) { selectedColor ->
                        viewModel.changeBackgroundColor(selectedColor)
                    }
                }
                holeColorPicker.setOnClickListener {
                    showColorPickerDialog(
                        requireContext(),
                        getString(R.string.hole_color_pick)
                    ) { selectedColor ->
                        viewModel.changeHoleColor(selectedColor)
                    }
                }
                valueTextColorPicker.setOnClickListener {
                    showColorPickerDialog(
                        requireContext(),
                        getString(R.string.value_text_color_pick)
                    ) { selectedColor ->
                        viewModel.changeValueTextColor(selectedColor)
                    }
                }
                labelTextColorPicker.setOnClickListener {
                    showColorPickerDialog(
                        requireContext(),
                        getString(R.string.labels_text_color_pick)
                    ) { selectedColor ->
                        viewModel.changeLabelTextColor(selectedColor)
                    }
                }
                transparentCircleColorPicker.setOnClickListener {
                    showColorPickerDialog(
                        requireContext(),
                        getString(R.string.circle_color_pick)
                    ) { selectedColor ->
                        viewModel.changeCircleColor(selectedColor)
                    }
                }
                centerTextColorPicker.setOnClickListener {
                    showColorPickerDialog(
                        requireContext(),
                        getString(R.string.center_text_color_pick)
                    ) { selectedColor ->
                        viewModel.changeCenterTextColor(selectedColor)
                    }
                }
                legendTextColorPicker.setOnClickListener {
                    showColorPickerDialog(
                        requireContext(),
                        getString(R.string.legend_text_color_pick)
                    ) { selectedColor ->
                        viewModel.changeLegendTextColor(selectedColor)
                    }
                }
            }
        }
    }

    private fun setupSwitches() {
        with(binding) {
            if (this != null) {
                switchPercentageDisplay.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.changeUsePercentages(true)
                    } else {
                        viewModel.changeUsePercentages(false)
                    }
                }
                switchShowLabels.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        labelOptionsGroup.visibility = View.VISIBLE
                        viewModel.changeDisplayLabels(true)
                    } else {
                        labelOptionsGroup.visibility = View.GONE
                        viewModel.changeDisplayLabels(false)
                    }
                }
                switchCenterText.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        centerTextOptionsGroup.visibility = View.VISIBLE
                        viewModel.changeShowCenterText(true)
                    } else {
                        centerTextOptionsGroup.visibility = View.GONE
                        viewModel.changeShowCenterText(false)
                    }
                }
                switchLegend.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        legendOptionsGroup.visibility = View.VISIBLE
                        viewModel.changeShowLegend(true)
                    } else {
                        legendOptionsGroup.visibility = View.GONE
                        viewModel.changeShowLegend(false)
                    }
                }
                switchValueTextBold.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(isChecked, switchValueTextItalic.isChecked) { typeface ->
                        viewModel.changeValuesTypeface(typeface)
                    }
                }
                switchValueTextItalic.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(switchValueTextBold.isChecked, isChecked) { typeface ->
                        viewModel.changeValuesTypeface(typeface)
                    }
                }
                switchLabelTextBold.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(isChecked, switchLabelTextItalic.isChecked) { typeface ->
                        viewModel.changeLabelsTypeface(typeface)
                    }
                }
                switchLabelTextItalic.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(switchLabelTextBold.isChecked, isChecked) { typeface ->
                        viewModel.changeLabelsTypeface(typeface)
                    }
                }
                switchCenterTextBold.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(isChecked, switchCenterTextItalic.isChecked) { typeface ->
                        viewModel.changeCenterTextTypeface(typeface)
                    }
                }
                switchCenterTextItalic.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(switchCenterTextBold.isChecked, isChecked) { typeface ->
                        viewModel.changeCenterTextTypeface(typeface)
                    }
                }
                switchLegendTextBold.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(isChecked, switchLegendTextItalic.isChecked) { typeface ->
                        viewModel.changeLegendTypeface(typeface)
                    }
                }
                switchLegendTextItalic.setOnCheckedChangeListener { _, isChecked ->
                    updateTypeface(switchLegendTextBold.isChecked, isChecked) { typeface ->
                        viewModel.changeLegendTypeface(typeface)
                    }
                }
            }
        }
    }

    private fun setupRadioGroups() {
        with(binding) {
            if (this != null) {
                groupOrientation.setOnCheckedChangeListener { _, checkedId ->
                    when (checkedId) {
                        R.id.orientationHorizontal -> viewModel.changeLegendOrientation(Legend.LegendOrientation.HORIZONTAL)
                        R.id.orientationVertical -> viewModel.changeLegendOrientation(Legend.LegendOrientation.VERTICAL)
                    }
                }
                groupForm.setOnCheckedChangeListener { _, checkedId ->
                    when (checkedId) {
                        R.id.formCircle -> viewModel.changeLegendForm(Legend.LegendForm.CIRCLE)
                        R.id.formSquare -> viewModel.changeLegendForm(Legend.LegendForm.SQUARE)
                        R.id.formLine -> viewModel.changeLegendForm(Legend.LegendForm.LINE)
                    }
                }
            }
        }
    }

    private fun setupCenterTextEditing() {
        with(binding) {
            if (this != null) {
                tvCenterTextString.setOnClickListener {
                    val textForEditing = tvCenterTextString.text.toString().takeIf { it != getString(R.string.edit_center_text) } ?: ""
                    showEditTextDialog(
                        requireContext(),
                        getString(R.string.center_text),
                        textForEditing
                    ) {
                        viewModel.changeCentralText(it)
                    }
                }
                btnEditCenterText.setOnClickListener {
                    val textForEditing = tvCenterTextString.text.toString().takeIf { it != getString(R.string.edit_center_text) } ?: ""
                    showEditTextDialog(
                        requireContext(),
                        getString(R.string.center_text),
                        textForEditing
                    ) {
                        viewModel.changeCentralText(it)
                    }
                }
            }
        }
    }

    private fun updateDataUI(pieDataSet: PieDataSet) {
        with(binding) {
            if (this != null) {
                tvSliceSpaceValue.text = Utils.convertPixelsToDp(pieDataSet.sliceSpace).toInt().toString()
                valueTextColorPicker.setCardBackgroundColor(pieDataSet.valueTextColor)
                tvValueTextSizeValue.text = Utils.convertPixelsToDp(pieDataSet.valueTextSize).toInt().toString()
                switchValueTextBold.isChecked = isBold(pieDataSet.valueTypeface)
                switchValueTextItalic.isChecked = isItalic(pieDataSet.valueTypeface)
            }
        }
    }

    private fun initDataSeekBars(pieDataSet: PieDataSet) {
        with(binding) {
            if (this != null) {
                seekBarValueTextSize.progress = Utils.convertPixelsToDp(pieDataSet.valueTextSize).toInt()
                seekBarSliceSpace.progress = Utils.convertPixelsToDp(pieDataSet.sliceSpace).toInt()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateAppearanceUI(appearance: PieChartAppearance) {
        with(binding) {
            if (this != null) {
                backgroundColorPicker.setCardBackgroundColor(appearance.backgroundColor)
                labelTextColorPicker.setCardBackgroundColor(appearance.entryLabelColor)
                centerTextColorPicker.setCardBackgroundColor(appearance.centerTextColor)
                holeColorPicker.setCardBackgroundColor(appearance.holeColor)
                transparentCircleColorPicker.setCardBackgroundColor(appearance.transparentCircleColor)
                tvMaxAngleValue.text = "${appearance.maxAngle.toInt()}${getString(R.string.degree_symbol)}"
                tvHoleRadiusValue.text = appearance.holeRadius.toInt().toString()
                switchPercentageDisplay.isChecked = appearance.usePercentValues
                switchShowLabels.isChecked = appearance.setDrawEntryLabels
                tvLabelTextSizeValue.text = appearance.entryLabelTextSize.toInt().toString()
                switchLabelTextBold.isChecked = isBold(appearance.entryLabelTypeface)
                switchLabelTextItalic.isChecked = isItalic(appearance.entryLabelTypeface)
                tvTransparentCircleValue.text = appearance.transparentCircleRadius.toInt().toString()
                tvTransparentCircleVisibilityValue.text = appearance.transparentCircleAlpha.toString()
                switchCenterText.isChecked = appearance.showCenterText
                tvCenterTextString.text = appearance.centerText.ifEmpty { getText(R.string.edit_center_text) }
                tvCenterTextSizeValue.text = appearance.centerTextSize.toInt().toString()
                tvCenterTextRadiusValue.text = appearance.centerTextRadius.toInt().toString()
                switchCenterTextBold.isChecked = isBold(appearance.centerTextTypeface)
                switchCenterTextItalic.isChecked = isItalic(appearance.centerTextTypeface)
                tvOffsetRightValue.text = appearance.extraOffsetRight.toInt().toString()
                tvOffsetLeftValue.text = appearance.extraOffsetLeft.toInt().toString()
                tvOffsetTopValue.text = appearance.extraOffsetTop.toInt().toString()
                tvOffsetBottomValue.text = appearance.extraOffsetBottom.toInt().toString()
            }
        }
    }

    private fun initAppearanceSeekBars(appearance: PieChartAppearance) {
        with(binding) {
            if (this != null) {
                seekBarMaxAngle.progress = appearance.maxAngle.toInt()
                seekBarHoleRadius.progress = appearance.holeRadius.toInt()
                seekBarLabelTextSize.progress = appearance.entryLabelTextSize.toInt()
                seekBarTransparentCircle.progress = appearance.transparentCircleRadius.toInt()
                seekBarTransparentCircleVisibility.progress = appearance.transparentCircleAlpha
                seekBarCenterTextSize.progress = appearance.centerTextSize.toInt()
                seekBarCenterTextRadius.progress = appearance.centerTextRadius.toInt()
                seekBarOffsetLeft.progress = appearance.extraOffsetLeft.toInt()
                seekBarOffsetRight.progress = appearance.extraOffsetRight.toInt()
                seekBarOffsetTop.progress = appearance.extraOffsetTop.toInt()
                seekBarOffsetBottom.progress = appearance.extraOffsetBottom.toInt()
            }
        }
    }

    private fun updateLegendUI(legend: Legend) {
        with(binding) {
            if (this != null) {
                switchLegend.isChecked = legend.isEnabled
                tvLegendTextSizeValue.text =
                    Utils.convertPixelsToDp(legend.textSize).toInt().toString()
                tvLegendSizeValue.text = legend.formSize.toInt().toString()
                switchLegendTextBold.isChecked = isBold(legend.typeface)
                switchLegendTextItalic.isChecked = isItalic(legend.typeface)
                legendTextColorPicker.setCardBackgroundColor(legend.textColor)
                tvLegendFromFormValue.text = legend.formToTextSpace.toInt().toString()
                when (legend.verticalAlignment) {
                    Legend.LegendVerticalAlignment.BOTTOM -> seekBarVerticalPosition.progress =
                        0
                    Legend.LegendVerticalAlignment.CENTER -> seekBarVerticalPosition.progress =
                        1
                    Legend.LegendVerticalAlignment.TOP -> seekBarVerticalPosition.progress = 2
                    else -> {}
                }
                when (legend.horizontalAlignment) {
                    Legend.LegendHorizontalAlignment.LEFT -> seekBarHorizontalPosition.progress =
                        0
                    Legend.LegendHorizontalAlignment.CENTER -> seekBarHorizontalPosition.progress =
                        1
                    Legend.LegendHorizontalAlignment.RIGHT -> seekBarHorizontalPosition.progress =
                        2
                    else -> {}
                }
                when (legend.orientation) {
                    Legend.LegendOrientation.VERTICAL -> orientationVertical.isChecked = true
                    else -> {
                        orientationHorizontal.isChecked = true
                    }
                }
                when (legend.form) {
                    Legend.LegendForm.SQUARE -> formSquare.isChecked = true
                    Legend.LegendForm.LINE -> formLine.isChecked = true
                    Legend.LegendForm.CIRCLE -> formCircle.isChecked = true
                    else -> {}
                }
            }
        }
    }

    private fun initLegendSeekBars(legend: Legend) {
        with(binding) {
            if (this != null) {
                seekBarLegendTextSize.progress = Utils.convertPixelsToDp(legend.textSize).toInt()
                seekBarLegendSize.progress = legend.formSize.toInt()
                seekBarFromForm.progress = legend.formToTextSpace.toInt()
            }
        }
    }

}

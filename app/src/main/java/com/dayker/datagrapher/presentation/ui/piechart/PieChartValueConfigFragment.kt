package com.dayker.datagrapher.presentation.ui.piechart

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.ModalPieChartValueConfigBinding
import com.dayker.datagrapher.presentation.ui.piechart.models.PieChartValue
import com.dayker.datagrapher.presentation.ui.piechart.viewmodel.PieChartViewModel
import com.dayker.datagrapher.utils.Utils
import com.dayker.datagrapher.utils.Utils.calculatePercentage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PieChartValueConfigFragment : BottomSheetDialogFragment() {

    private var binding: ModalPieChartValueConfigBinding? = null
    private val viewModel: PieChartViewModel by activityViewModels()
    private val args: PieChartValueConfigFragmentArgs by navArgs()
    private val externalTotalAmount by lazy { PieChartValue.totalAmount - args.value }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModalPieChartValueConfigBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        setupDialog()

        binding?.cancel?.setOnClickListener {
            dismiss()
        }
        binding?.confirm?.setOnClickListener {
            if (isDataValid()) {
                confirmAction()
                dismiss()
            }
        }
        binding?.colorPicker?.setOnClickListener {
            Utils.showColorPickerDialog(
                requireContext(),
                getString(R.string.pick_color)
            ) { selectedColor ->
                binding?.colorPicker?.setCardBackgroundColor(Color.parseColor("#$selectedColor"))
            }
        }
        binding?.deleteLayout?.setOnClickListener {
            viewModel.deleteEntry(args.position)
            dismiss()
        }
        binding?.editTextValue?.setOnFocusChangeListener { _, focused ->
            binding?.textInputLayoutValue?.helperText =
                if (!focused) {
                    validValue()
                } else null
        }
        binding?.editTextLabel?.setOnFocusChangeListener { _, focused ->
            binding?.textInputLayoutLabel?.helperText =
                if (!focused) {
                    validLabel()
                } else null
        }
        binding?.editTextValue?.addTextChangedListener(editTextValueWatcher)
    }

    private fun validValue(): String? {
        return if (binding?.editTextValue?.text.toString().trim().isEmpty()) {
            getString(R.string.value_is_empty)
        } else if (binding?.editTextValue?.text.toString().trim().toFloat() == 0f) {
            getString(R.string.value_is_0)
        } else null
    }

    private fun validLabel(): String? {
        return if (binding?.editTextLabel?.text.toString().trim().isEmpty()) {
            getString(R.string.label_is_empty)
        } else null
    }

    private fun setupDialog() {
        with(binding) {
            if (this != null) {
                tvTitle.text = args.title
                editTextValue.setText(args.value.toString())
                editTextLabel.setText(args.label)
                colorPicker.setCardBackgroundColor(args.color)
                tvPercentageValue.text = calculatePercentage(args.value, PieChartValue.totalAmount)
                if (args.position < 0) {
                    deleteLayout.visibility = View.GONE
                }
            }
        }
    }

    /**
     * If [args.position] is greater than or equal to zero, the .updateEntry() method is called
     * to update an existing entry. Otherwise, the addEntry() method is called to add a new entry.
     */
    private fun confirmAction() {
        with(binding) {
            if (this != null) {
                val value = editTextValue.text.toString().toFloat()
                val label = editTextLabel.text.toString()
                val color = colorPicker.cardBackgroundColor.defaultColor
                if (args.position >= 0) {
                    viewModel.updateEntry(
                        index = args.position,
                        value = value,
                        label = label,
                        color = color
                    )
                } else {
                    viewModel.addEntry(value = value, label = label, color = color)
                }
            }
        }
    }

    private fun isDataValid(): Boolean {
        with(binding) {
            if (this != null) {
                textInputLayoutValue.helperText = validValue()
                textInputLayoutLabel.helperText = validLabel()
                return textInputLayoutLabel.helperText == null && textInputLayoutValue.helperText == null
            }
        }
        return false
    }

    private val editTextValueWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updatePercentage()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun updatePercentage() {
        val newValue = binding?.editTextValue?.text.toString().toFloatOrNull() ?: 0f
        val totalValue = externalTotalAmount + newValue
        binding?.tvPercentageValue?.text = calculatePercentage(newValue, totalValue)
    }
}




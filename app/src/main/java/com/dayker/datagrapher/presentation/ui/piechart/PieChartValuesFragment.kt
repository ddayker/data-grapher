package com.dayker.datagrapher.presentation.ui.piechart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.FragmentPieChartValuesBinding
import com.dayker.datagrapher.presentation.ui.piechart.adapter.PieChartValuesAdapter
import com.dayker.datagrapher.presentation.ui.piechart.models.PieChartValue
import com.dayker.datagrapher.presentation.ui.piechart.viewmodel.PieChartViewModel
import com.dayker.datagrapher.utils.UIUtilities.showEditTextDialog
import com.dayker.datagrapher.utils.Utilities
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PieChartValuesFragment : Fragment() {

    private var binding: FragmentPieChartValuesBinding? = null
    private val viewModel: PieChartViewModel by activityViewModels()
    private val navController by lazy { findNavController() }
    private var adapter: PieChartValuesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPieChartValuesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        initRecyclerView()

        viewModel.pieChartValues.observe(this) { pieChartValues ->
            PieChartValue.totalAmount = pieChartValues.sumOf { it.value.toDouble() }.toFloat()
            adapter?.updateData(pieChartValues)
        }
        viewModel.pieDataSet.observe(this) { dataSet ->
            binding?.chartNameValue?.text = dataSet.label
        }
        binding?.addNewEntry?.setOnClickListener {
            val action = PieChartValueConfigFragmentDirections.actionShowBottomSheetDialog(
                title = getString(R.string.add_a_new_entry)
            )
            navController.navigate(action)
        }
        binding?.btnEdit?.setOnClickListener {
            showEditTextDialog(
                requireContext(),
                getString(R.string.set_chart_name),
                binding?.chartNameValue?.text.toString()
            ) {
                viewModel.setChartName(it)
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = layoutManager
        adapter = PieChartValuesAdapter(emptyList(), navController)
        binding?.recyclerView?.adapter = adapter
    }
}
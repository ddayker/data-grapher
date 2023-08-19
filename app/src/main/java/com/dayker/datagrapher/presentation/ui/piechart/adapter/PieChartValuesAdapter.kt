package com.dayker.datagrapher.presentation.ui.piechart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.dayker.datagrapher.R
import com.dayker.datagrapher.databinding.ItemPieChartBinding
import com.dayker.datagrapher.presentation.ui.diffutil.DiffUtilCallback
import com.dayker.datagrapher.presentation.ui.piechart.PieChartValueConfigFragmentDirections
import com.dayker.datagrapher.presentation.ui.piechart.models.PieChartValue
import com.dayker.datagrapher.utils.Utils.calculatePercentage

class PieChartValuesAdapter(
    private var dataSet: List<PieChartValue>,
    private val navController: NavController
) :
    RecyclerView.Adapter<PieChartValuesAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ItemPieChartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: PieChartValue) {
            with(binding) {
                tvLabel.text = item.label
                tvValue.text = item.value.toString()
                tvNumber.text =
                    itemView.context.getString(R.string.numero_symbol) + (adapterPosition + 1)
                tvPercentage.text = calculatePercentage(item.value, PieChartValue.totalAmount)
                viewColor.setCardBackgroundColor(item.color)
            }
        }
    }

    fun updateData(newData: List<PieChartValue>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffUtilCallback(
                oldData = dataSet,
                newData = newData,
                areItemsTheSame = { oldItem, newItem ->
                    oldItem == newItem
                },
                areContentsTheSame = { oldItem, newItem ->
                    oldItem == newItem
                }
            )
        )
        dataSet = newData
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPieChartBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.itemView.setOnClickListener {
            val action = PieChartValueConfigFragmentDirections.actionShowBottomSheetDialog(
                title = holder.itemView.context.getString(R.string.change_entry_n) + (position + 1).toString(),
                value = dataSet[position].value,
                label = dataSet[position].label,
                color = dataSet[position].color,
                position = position
            )
            navController.navigate(action)
        }
    }

    override fun getItemCount() = dataSet.size

}

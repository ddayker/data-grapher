package com.dayker.datagrapher.presentation.ui.diffutil

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<T>(
    private val oldData: List<T>,
    private val newData: List<T>,
    private val areItemsTheSame: (T, T) -> Boolean,
    private val areContentsTheSame: (T, T) -> Boolean
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldData[oldItemPosition], newData[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldData[oldItemPosition], newData[newItemPosition])
    }
}

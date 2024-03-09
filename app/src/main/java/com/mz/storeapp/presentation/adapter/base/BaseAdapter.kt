package com.mz.storeapp.presentation.adapter.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * Base class for adapters.
 *
 * @param E The type of the items in the adapter.
 * @param itemCallback The DiffUtil.ItemCallback for the adapter.
 */
abstract class BaseAdapter<E : Any>(itemCallback: DiffUtil.ItemCallback<E>) :
    ListAdapter<E, BaseViewHolder<out ViewDataBinding, E>>(itemCallback) {
    /**
     * Binds the data to the view holder.
     *
     * @param holder The view holder.
     * @param position The position of the item in the adapter.
     */
    override fun onBindViewHolder(holder: BaseViewHolder<out ViewDataBinding, E>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}
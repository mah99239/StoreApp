package com.mz.storeapp.presentation.adapter.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Base class for all view holders.
 *
 * @param VB The type of the view binding.
 * @param E The type of the element to be displayed.
 */
abstract class BaseViewHolder<VB : ViewDataBinding, E : Any>(protected open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    /**
     * The context of the view holder.
     */
    val context: Context
        get() {
            return itemView.context
        }

    /**
     * The element to be displayed.
     */
    lateinit var element: E

    /**
     * Binds the element to the view holder.
     *
     * @param element The element to be displayed.
     */
    open fun bind(element: E) {
        this.element = element
    }
}
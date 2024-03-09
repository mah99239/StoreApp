package com.mz.storeapp.presentation.adapter

import android.animation.ObjectAnimator
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mz.storeapp.R
import com.mz.storeapp.data.model.remote.ProductItem
import com.mz.storeapp.databinding.ItemProductBinding
import com.mz.storeapp.presentation.adapter.base.BaseAdapter
import com.mz.storeapp.presentation.adapter.base.BaseViewHolder
import timber.log.Timber

class ProductsAdapter(private val itemClick: (idProduct: Int) -> Unit) :
    BaseAdapter<ProductItem>(ProductsDiffCallback) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ProductItem]
     * has been updated.
     */
    companion object ProductsDiffCallback : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {

            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<out ViewDataBinding, ProductItem> {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), itemClick
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<out ViewDataBinding, ProductItem>,
        position: Int
    ) {
        if (getItem(position) == null) {
            (holder as ProductViewHolder).showPlaceholder()
        } else {
            super.onBindViewHolder(holder, position)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product
    }

    /**
     *  parent list is immutable and override here to use mutableList.
     */
    override fun submitList(list: List<ProductItem>?) {
        Timber.e("list of ProductItem = $list")
        super.submitList(
            list?.let { ArrayList(it) })
    }
    /**
     * ViewHolder for Product items. All work is done by data binding.
     */
    class ProductViewHolder(
        binding: ItemProductBinding,
        private val itemClick: (idProduct: Int) -> Unit
    ) :
        BaseViewHolder<ItemProductBinding, ProductItem>(binding) {


        /**
         * This is the animation we apply to each of the list items. It animates the alpha value from 1
         * to 0, then back to 1. The animation repeats infinitely until it is manually ended.
         */
        private val animation = ObjectAnimator.ofFloat(itemView, View.ALPHA, 1f, 0f, 1f).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = FADE_DURATION
            // Reset the alpha on animation end.
            doOnEnd { itemView.alpha = 1f }
        }

        fun showPlaceholder() {
            // Shift the timing of fade-in/out for each item by its adapter position. We use the
            // elapsed real time to make this independent from the timing of method call.
            animation.currentPlayTime =
                (SystemClock.elapsedRealtime() - adapterPosition * 30L) % FADE_DURATION
            animation.start()
            // Show the placeholder UI.
            binding.apply {
                imvProduct.setImageResource(R.drawable.image_placeholder)
                tvProductPrice.text = null
                tvProductName.text = null
                tvProductName.setBackgroundResource(R.drawable.text_placeholder)
                tvProductPrice.setBackgroundResource(R.drawable.text_placeholder)
            }

        }

        override fun bind(element: ProductItem) {
            super.bind(element)
            animation.end()
            binding.apply {
                product = element
                tvProductName.setBackgroundResource(0)
                tvProductPrice.setBackgroundResource(0)
                itemView.setOnClickListener {
                    product?.run { itemClick(this.id) }
                }
            }


            Timber.d("element = $element")

        }

    }

}

/**
 * A dummy adapter that shows placeholders.
 */
internal class PlaceholderAdapter(private val itemClick: (idProduct: Int) -> Unit) :
    RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsAdapter.ProductViewHolder {
        return ProductsAdapter.ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), itemClick
        )
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ProductViewHolder, position: Int) {
        // We have to call this method in onBindVH rather than onCreateVH because it uses the
        // adapterPosition of the ViewHolder.
        holder.showPlaceholder()
    }
}

/**
 * Duration of the animation used for fading out a placeholder item.
 */
private const val FADE_DURATION = 1000L
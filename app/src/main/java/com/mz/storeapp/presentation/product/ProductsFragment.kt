package com.mz.storeapp.presentation.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import com.mz.storeapp.R
import com.mz.storeapp.databinding.FragmentProductsBinding
import com.mz.storeapp.presentation.adapter.PlaceholderAdapter
import com.mz.storeapp.presentation.adapter.ProductsAdapter
import com.mz.storeapp.presentation.utils.FAST_OUT_SLOW_IN
import com.mz.storeapp.presentation.utils.LARGE_EXPAND_DURATION
import com.mz.storeapp.presentation.utils.plusAssign
import com.mz.storeapp.presentation.utils.setupRefreshLayout
import com.mz.storeapp.presentation.utils.transitionSequential
import com.mz.storeapp.presentation.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * A fragment that displays a list of products.
 */
@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    val binding get() = _binding!!

    private val viewModel: ProductsViewModel by activityViewModels()
    private val placeholderAdapter =
        PlaceholderAdapter {
        }
    private val productsAdapter: ProductsAdapter by lazy {
        ProductsAdapter {
            viewModel.setItemProductSelected(it)
            requireView().findNavController()
                .navigate(R.id.action_productsFragment_to_productInfoFragment)
        }
    }
    private var savedItemAnimator: RecyclerView.ItemAnimator? = null

    private val fade =
        transitionSequential {
            duration = LARGE_EXPAND_DURATION
            interpolator = FAST_OUT_SLOW_IN

            plusAssign(Fade(Fade.OUT))
            plusAssign(Fade(Fade.IN))
            addListener(
                object : TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition) {
                        if (savedItemAnimator != null) {
                            binding.rvProducts.itemAnimator = savedItemAnimator
                        }
                    }
                },
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        setup()
    }

    /**
     * Sets up the fragment.
     */
    private fun setup() {
        setupAdapter()
        setupObserverState()
        setupRefreshLayout()
    }

    /**
     * Sets up the adapter for the product list.
     */
    private fun setupAdapter() {
        binding.rvProducts.adapter = placeholderAdapter
        setupObserverProductsWithAnimator()
    }

    /**
     * Sets up the observer for the products with animator.
     */
    private fun setupObserverProductsWithAnimator() {
        Timber.e("setupObserverProductsWithAnimator")
        viewModel.products.observe(viewLifecycleOwner) {
            stopAnimator()
            binding.products = it
        }
    }

    /**
     * Stops the animator.
     */
    private fun stopAnimator() {
        if (binding.rvProducts.adapter != productsAdapter) {
            binding.rvProducts.adapter = productsAdapter
            savedItemAnimator = binding.rvProducts.itemAnimator
            binding.rvProducts.itemAnimator = null
            TransitionManager.beginDelayedTransition(binding.rvProducts, fade)
        }
    }

    /**
     * Sets up the observer for the state.
     */
    private fun setupObserverState() {
        viewModel.fetchProducts()
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.isLoading = it
        }
        viewModel.isEmptyProducts.observe(viewLifecycleOwner) {
            binding.isEmpty = it
            Timber.e("isEmpty: $it")
            if (it) stopAnimator()
        }
        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.isRefreshing = it
        }
    }

    /**
     * Sets up the refresh layout.
     */
    private fun setupRefreshLayout() {
        setupRefreshLayout(binding.refreshLayout, binding.rvProducts)
        binding.refreshLayout.setOnRefreshListener {
            refreshAdapter()
        }
    }

    /**
     * Refreshes the adapter.
     */
    private fun refreshAdapter() {
        TransitionManager.beginDelayedTransition(binding.rvProducts, fade)
        binding.rvProducts.adapter = placeholderAdapter
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
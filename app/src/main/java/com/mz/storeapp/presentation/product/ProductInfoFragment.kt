package com.mz.storeapp.presentation.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mz.storeapp.databinding.FragmentProductInfoBinding
import com.mz.storeapp.presentation.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductInfoFragment : Fragment() {
    private var _binding: FragmentProductInfoBinding? = null
    val binding get() = _binding!!

    private val viewModel: ProductsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.isLoadingProductItem.observe(viewLifecycleOwner) {
            binding.isLoading = it
        }
        viewModel.productItem.observe(viewLifecycleOwner) {
            binding.product = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
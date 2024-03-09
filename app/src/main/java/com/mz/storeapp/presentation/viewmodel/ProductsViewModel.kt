package com.mz.storeapp.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mz.storeapp.R
import com.mz.storeapp.data.model.remote.ProductItem
import com.mz.storeapp.data.repository.ResourceProducts
import com.mz.storeapp.data.utils.NetworkMonitor
import com.mz.storeapp.data.utils.Resource
import com.mz.storeapp.domain.usecases.GetAllProductsUseCase
import com.mz.storeapp.domain.usecases.GetProductItemUseCase
import com.mz.storeapp.presentation.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * A ViewModel that handles the logic for the Products screen.
 *
 * @param networkMonitor The NetworkMonitor to monitor the network connectivity.
 * @param getProductsUseCase The use case to get all products.
 * @param getProductItemUseCase The use case to get a product item.
 */
@HiltViewModel
class ProductsViewModel
    @Inject
    constructor(
        private val networkMonitor: NetworkMonitor,
        private val getProductsUseCase: GetAllProductsUseCase,
        private val getProductItemUseCase: GetProductItemUseCase,
    ) : ViewModel() {
        // Observables
        private val _isOnline: MutableLiveData<Boolean> = MutableLiveData(false)
        val isOnline get() = _isOnline
        private val _userMessage: MutableLiveData<Event<Int>> = MutableLiveData()
        val userMessage get() = _userMessage

        private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
        val isLoading: LiveData<Boolean> get() = _isLoading
        private val _isEmptyProducts: MutableLiveData<Boolean> = MutableLiveData(false)
        val isEmptyProducts get() = _isEmptyProducts
        private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)
        val isRefreshing get() = _isRefreshing

        private val _products: MutableLiveData<List<ProductItem>> = MutableLiveData()
        val products get() = _products

        private val _isLoadingProductItem: MutableLiveData<Boolean> = MutableLiveData(false)
        val isLoadingProductItem get() = _isLoadingProductItem
        private val _productItem: MutableLiveData<ProductItem> = MutableLiveData()
        val productItem get() = _productItem

        init {
            setNetworkMonitor()
        }

        /**
         * Sets up the network monitor to listen for changes in network connectivity.
         */
        private fun setNetworkMonitor() =
            viewModelScope.launch {
                networkMonitor.isOnline.collect {
                    setConnected(it)
                }
            }

        /**
         * Updates the `_isOnline` observable with the current network connectivity status.
         *
         * @param isOnline The current network connectivity status.
         */
        private fun setConnected(isOnline: Boolean) {
            _isOnline.value = isOnline
        }

        /**
         * Shows a snackbar message with the given resource ID.
         *
         * @param message The resource ID of the message to show.
         */
        private fun showSnackbarMessage(
            @StringRes message: Int,
        ) {
            Timber.e("message: $message")
            _userMessage.value = Event(message)
        }

        /**
         * Fetches the products from the server or cache.
         */
        fun fetchProducts() {
            viewModelScope.launch {
                getProductsUseCase().collect {
                    handleFetchProducts(it)
                }
            }
        }

        private fun handleFetchProducts(it: ResourceProducts) {
            when (it) {
                is Resource.Error -> {
                    val message = it.exception as? Int ?: R.string.all_error_unknown
                    showSnackbarMessage(message)
                    if (products.value.isNullOrEmpty()) {
                        showEmpty()
                    } else {
                        hideEmpty()
                    }
                    hideLoading()
                    _isRefreshing.value = false
                }

                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    _isRefreshing.value = false
                    hideLoading()
                    if (it.data.isEmpty()) {
                        showEmpty()
                    } else {
                        _products.value = ArrayList(it.data)
                        hideEmpty()
                    }
                }
            }
        }

        /**
         * Shows the empty state.
         */
        private fun showEmpty() {
            isEmptyProducts.value = true
        }

        /**
         * Hides the empty state.
         */
        private fun hideEmpty() {
            isEmptyProducts.value = false
        }

        /**
         * Hides the loading indicator.
         */
        private fun hideLoading() {
            _isLoading.value = false
        }

        /**
         * Shows the loading indicator.
         */
        private fun showLoading() {
            _isLoading.value = true
        }

        /**
         * Refreshes the products.
         */
        fun refresh() {
            _isRefreshing.value = true
            getProductsUseCase.refreshProducts()
            fetchProducts()
        }

        /**
         * Sets the selected product.
         *
         * @param id The ID of the selected product.
         */
        fun setItemProductSelected(id: Int) {
            getItemProductSelected(id)
        }

        /**
         * Gets the product item with the given ID.
         *
         * @param id The ID of the product item.
         */
        private fun getItemProductSelected(id: Int) {
            Timber.i("id: $id")

            _isLoadingProductItem.value = true
            viewModelScope.launch {
                delay(600L)
                getProductItemUseCase(id).collect {
                    Timber.i("item $it")
                    _productItem.value = it
                    _isLoadingProductItem.value = false
                }
            }
        }
    }
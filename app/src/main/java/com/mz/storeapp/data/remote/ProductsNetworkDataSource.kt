package com.mz.storeapp.data.remote

import com.mz.storeapp.data.model.remote.ProductItem

/**
 * Interface representing network calls to the Store backend
 */
interface ProductsNetworkDataSource {
    suspend fun getProducts(): List<ProductItem>

    suspend fun getProduct(productId: Int): ProductItem
}
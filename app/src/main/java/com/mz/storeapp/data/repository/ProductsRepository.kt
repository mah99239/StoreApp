package com.mz.storeapp.data.repository

import com.mz.storeapp.data.model.remote.ProductItem
import com.mz.storeapp.data.utils.Resource
import kotlinx.coroutines.flow.Flow

typealias ResourceProducts = Resource<List<ProductItem>>

interface ProductsRepository {
    fun getProducts(): Flow<ResourceProducts>

    fun getProductItem(idProduct: Int): Flow<ProductItem>

    fun refreshProductsCache(): Unit
}
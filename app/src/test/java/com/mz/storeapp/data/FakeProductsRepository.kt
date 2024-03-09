package com.mz.storeapp.data

import com.mz.storeapp.data.model.remote.ProductItem
import com.mz.storeapp.data.model.remote.Rating
import com.mz.storeapp.data.repository.ProductsRepository
import com.mz.storeapp.data.repository.ResourceProducts
import com.mz.storeapp.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before

class FakeProductsRepository : ProductsRepository {


    private var isCacheDirty = false
    private var shouldReturnError = false
    private var shouldReturnEmpty = false
    val fakeProducts = listOf(
        ProductItem(
            1, "category", "description",
            "ImageURL", 22.22, Rating(2, 2.2), "title"
        ),
        ProductItem(
            2, "category2", "description2",
            "ImageURL2", 22.22, Rating(222, 4.2), "titleX"
        )
    )

    @Before
    fun setup() {
        shouldReturnError = false
        shouldReturnEmpty = false
    }

    override fun getProducts(): Flow<ResourceProducts> {
        if (shouldReturnError) {

            return flowOf( Resource.Error("Error"))
        }
        if (shouldReturnEmpty) return flowOf(Resource.Success(listOf()))

        return flowOf(Resource.Success(fakeProducts))
    }

    override fun getProductItem(idProduct: Int): Flow<ProductItem> {
        return flowOf(fakeProducts[0])
    }

    override fun refreshProductsCache() {
        isCacheDirty = true
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setReturnEmpty(value: Boolean) {
        shouldReturnEmpty = value
    }
}
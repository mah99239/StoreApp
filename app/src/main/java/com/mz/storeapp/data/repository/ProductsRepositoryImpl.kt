package com.mz.storeapp.data.repository

import com.mz.storeapp.R
import com.mz.storeapp.data.db.ProductsDao
import com.mz.storeapp.data.mapper.asEntity
import com.mz.storeapp.data.mapper.asExternalModel
import com.mz.storeapp.data.model.db.ProductEntity
import com.mz.storeapp.data.model.remote.ProductItem
import com.mz.storeapp.data.remote.ProductsNetworkDataSource
import com.mz.storeapp.di.utils.AppDispatchers.IO
import com.mz.storeapp.di.utils.Dispatcher
import com.mz.storeapp.data.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Repository implementation for accessing product data.
 *
 * @param productsApi The network data source for products.
 * @param productsDao The local data source for products.
 * @param ioDispatcher The coroutine dispatcher for IO operations.
 */
class ProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsNetworkDataSource,
    private val productsDao: ProductsDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ProductsRepository {
    private var isCacheDirty = false

    /**
     * Gets a flow of [ResourceProducts] representing the list of products.
     *
     * @return A flow of [ResourceProducts].
     */
    override fun getProducts(): Flow<ResourceProducts> = flow<ResourceProducts> {
        emit(Resource.Loading)
        delay(1000L)
        val localProducts = productsDao.getProductsEntities()
        if (!isCacheDirty) {
            // Emit local products immediately, even if empty
            emit(Resource.Success(localProducts.map { it.asExternalModel() }))
        }

        // Check if cache is empty (regardless of dirty flag)
        if (localProducts.isEmpty() || isCacheDirty) {
            try {
                // Fetch remote products only when local is empty
                val remoteProducts = productsApi.getProducts()
                productsDao.insertProducts(remoteProducts.map(ProductItem::asEntity))
                isCacheDirty = false // Mark cache as fresh

                emit(Resource.Success(remoteProducts))
            } catch (e: Exception) {
                handleGetProductsExecption(e, localProducts)

            }
        }
    }.flowOn(ioDispatcher)

    /**
     * Handles exceptions thrown during the [getProducts] operation.
     *
     * @param e The exception that was thrown.
     * @param localProducts The list of local products.
     */
    private suspend fun FlowCollector<ResourceProducts>.handleGetProductsExecption(
        e: Exception,
        localProducts: List<ProductEntity>
    ) {
        when (e) {
            is IOException, is SocketTimeoutException -> {
                if (localProducts.isNotEmpty()) {
                    emit(Resource.Error(R.string.error_fetch_remote_data))
                    emit(Resource.Success(localProducts.map { it.asExternalModel() }))
                } else {
                    emit(Resource.Error(R.string.network_error))
                }
            }

            else -> {
                if (localProducts.isNotEmpty()) {
                    emit(Resource.Error(R.string.error_fetch_remote_data_unknown))
                    emit(Resource.Success(localProducts.map { it.asExternalModel() }))
                } else {
                    emit(Resource.Error(R.string.all_error_unknown))
                }
            }
        }
    }


    /**
     * Gets a flow of [ProductItem] for the given product ID.
     *
     * @param idProduct The ID of the product.
     * @return A flow of [ProductItem].
     */
    override fun getProductItem(idProduct: Int): Flow<ProductItem> {
        return productsDao.getProductEntity(idProduct).map { it.asExternalModel() }
            .flowOn(ioDispatcher)
    }

    /**
     * Refreshes the list of [ProductItem] by fetching the [ProductItem] from the remote API and updating
     * the local database.
     */
    override fun refreshProductsCache() {
        isCacheDirty = true
    }
}
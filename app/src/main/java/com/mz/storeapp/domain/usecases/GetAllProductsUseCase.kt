package com.mz.storeapp.domain.usecases

import com.mz.storeapp.data.repository.ProductsRepository
import javax.inject.Inject

/**
 * A use case that gets all products from the repository.
 *
 * @param productsRepository The repository to get the products from Api or Cache.
 */
class GetAllProductsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    /**
     * Invokes the use case and returns the products.
     */
    operator fun invoke() = productsRepository.getProducts()

    /**
     * Refreshes the products in the cache.
     */
    fun refreshProducts() = productsRepository.refreshProductsCache()
}
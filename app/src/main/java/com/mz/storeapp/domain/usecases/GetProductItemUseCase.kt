package com.mz.storeapp.domain.usecases

import com.mz.storeapp.data.repository.ProductsRepository
import javax.inject.Inject

class GetProductItemUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
     operator fun invoke(productId: Int) = productsRepository.getProductItem(productId)
}
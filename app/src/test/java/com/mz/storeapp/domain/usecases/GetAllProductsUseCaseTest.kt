package com.mz.storeapp.domain.usecases

import com.mz.storeapp.data.FakeProductsRepository
import com.mz.storeapp.data.utils.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GetAllProductsUseCaseTest {
    private var productRepository: FakeProductsRepository = FakeProductsRepository()

    // subject under test
    private val getAllProductsUseCase = GetAllProductsUseCase(productRepository)

    @Test
    fun invoke_whenGetAllProducts_returnResourceSuccessListOfProducts() = runTest {
        // When invoke is called
        val products = getAllProductsUseCase.invoke().first()

        // Then the result should be a list of products
        assertThat(products, `is`(Resource.Success(productRepository.fakeProducts)))

    }

    @Test
    fun invoke_whenGetAllProducts_returnResourceOfEmptyList() = runTest {
        // When invoke is called
        productRepository.setReturnEmpty(true)
        val products = getAllProductsUseCase.invoke().first()
        // Then - the result should be an empty list
        assertThat(products, `is`(Resource.Success(emptyList())))
    }

    @Test
    fun invoke_whenException_thenReturnResourceError() = runTest {
        productRepository.setReturnError(true)
        // When invoke is called
        val exception = getAllProductsUseCase.invoke().first()
        // Then - the result of Resource should be an error
        assertThat((exception), `is`(Resource.Error("Error")))

    }
}
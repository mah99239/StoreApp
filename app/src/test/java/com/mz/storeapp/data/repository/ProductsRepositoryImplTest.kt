package com.mz.storeapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mz.storeapp.R
import com.mz.storeapp.data.db.ProductsDao
import com.mz.storeapp.data.mapper.asEntity
import com.mz.storeapp.data.mapper.asExternalModel
import com.mz.storeapp.data.model.db.ProductEntity
import com.mz.storeapp.data.model.remote.ProductItem
import com.mz.storeapp.data.model.remote.Rating
import com.mz.storeapp.data.remote.ProductsNetworkDataSource
import com.mz.storeapp.data.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ProductsRepositoryImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule() // Forces coroutines to run synchronously

    private val productApiMock = mockk<ProductsNetworkDataSource>()
    private val productDaoMockk = mockk<ProductsDao>()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope: TestScope = TestScope(testDispatcher)

    // Subject under test
    private val repository: ProductsRepositoryImpl = ProductsRepositoryImpl(
        productApiMock, productDaoMockk, testDispatcher
    )

    @Test
    fun `getProducts - empty local data - fetches from network and emits local data`() =
        testScope.runTest {
            // Mock empty local data
            coEvery { productDaoMockk.getProductsEntities() } returns listOf<ProductEntity>()
            coEvery { productDaoMockk.insertProducts(fakeProducts().map(ProductItem::asEntity)) } returns Unit

            // Mock successful network response
            val expectedRemoteProducts = fakeProducts()
            coEvery { productApiMock.getProducts() } returns expectedRemoteProducts

            // Launch the flow and collect results

            val testFlow = repository.getProducts()
            advanceUntilIdle()
            val result = testFlow.last()

            // Assert values emitted
            assertThat(result, `is`(Resource.Success(expectedRemoteProducts)))

            // Verify network call and database insertion
            coVerify { productApiMock.getProducts() }
            coVerify { productDaoMockk.insertProducts(expectedRemoteProducts.map { it.asEntity() }) }
        }

    @Test
    fun `getProducts - non-empty local data - fetches from network (cache dirty)`() =
        testScope.runTest {
            // Mock local data
            coEvery { productDaoMockk.getProductsEntities() } returns fakeProducts().map(ProductItem::asEntity)
            coEvery { productDaoMockk.insertProducts(fakeProducts().map(ProductItem::asEntity)) } returns Unit

            // Mock successful network response
            val expectedRemoteProducts = fakeProducts()
            coEvery { productApiMock.getProducts() } returns expectedRemoteProducts
            // Set cache dirty flag (implementation specific)

            repository.refreshProductsCache()
            // Launch the flow and collect results

            val testFlow = repository.getProducts()
            val result = testFlow.last()

            // Assert values emitted
            assertThat(result, `is`(Resource.Success(expectedRemoteProducts)))

            // Verify network call and database insertion
            coVerify { productApiMock.getProducts() }
            coVerify { productDaoMockk.insertProducts(expectedRemoteProducts.map { it.asEntity() }) }
        }

    @Test
    fun `getProducts - non-empty local data - emits local data only (cache not dirty)`() =
        testScope.runTest {
            val localProducts = fakeProducts().map { it.asEntity() }

            // Stub local data
            coEvery { productDaoMockk.getProductsEntities() } returns localProducts

            val testFlow = repository.getProducts()

            val results = testFlow.last()

            // Assert only local data is emitted
            assertThat(results, `is`(Resource.Success(localProducts.map { it.asExternalModel() })))


        }

    @Test
    fun `getProducts - when refresh Cache and network fails - then fetches from local data`() =
        testScope.runTest {
            // Mock empty local data
            coEvery { productDaoMockk.getProductsEntities() } returns fakeProducts().map(ProductItem::asEntity)
            coEvery { productApiMock.getProducts() } throws IOException("Unexpected error")
            // coEvery { productDaoMockk.insertProducts(fakeProducts().map(ProductItem::asEntity)) } returns Unit

            // Mock successful network response
            val expectedRemoteProducts = fakeProducts()

            // When - refresh cache and Launch the flow and collect results
            repository.refreshProductsCache()
            val resultProducts = repository.getProducts()

            // Then - fetches from local data
            val resultFlow = resultProducts.catch<Any> { emit(it) }.toList()
            assertThat(resultFlow[0], `is`(Resource.Loading))
            assertThat(resultFlow[1], `is`(Resource.Error(R.string.error_fetch_remote_data)))
            assertThat(resultFlow[2], `is`(Resource.Success(expectedRemoteProducts)))


            // Verify network call and database insertion
            coVerify { productApiMock.getProducts() }
        }

    private fun fakeProducts() = listOf(
        ProductItem(
            1, "category", "description", "ImageURL", 22.22, Rating(2, 2.2), "title"
        ), ProductItem(
            2, "category2", "description2", "ImageURL2", 22.22, Rating(222, 4.2), "titleX"
        )
    )
}
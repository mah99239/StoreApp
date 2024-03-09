package com.mz.storeapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mz.storeapp.R
import com.mz.storeapp.data.FakeProductsRepository
import com.mz.storeapp.data.utils.NetworkMonitor
import com.mz.storeapp.domain.usecases.GetAllProductsUseCase
import com.mz.storeapp.domain.usecases.GetProductItemUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Make LiveData work instantly

    // Subject under test
    private lateinit var viewModel: ProductsViewModel
    private lateinit var mockNetworkMonitor: NetworkMonitor
    private lateinit var getProductsUseCase: GetAllProductsUseCase
    private lateinit var getProductItemUseCase: GetProductItemUseCase
    private lateinit var fakeProductsRepository: FakeProductsRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope: TestScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeProductsRepository = FakeProductsRepository()

        mockNetworkMonitor = mockk()
        getProductsUseCase = GetAllProductsUseCase(fakeProductsRepository)
        getProductItemUseCase = GetProductItemUseCase(fakeProductsRepository)
        viewModel =
            ProductsViewModel(
                mockNetworkMonitor,
                getProductsUseCase,
                getProductItemUseCase,
            )
        coEvery { mockNetworkMonitor.isOnline } returns flowOf(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchProducts should update products when successful`() =
        testScope.runTest {
            // When _ fetchProducts is called
            viewModel.fetchProducts()
            // Execute pending coroutines actions
            advanceUntilIdle()
            // Then
            assertThat(viewModel.products.value, `is`(fakeProductsRepository.fakeProducts))
        }

    @Test
    fun `fetchProducts should show error message when error occurs`() =
        testScope.runTest {
            // Given - an error
            fakeProductsRepository.setReturnError(true)

            // When _ fetchProducts is called
            viewModel.fetchProducts()

            // Execute pending coroutines actions
            advanceUntilIdle()
            // Then - products should be emitted and isLoading should be set to false
            assert(viewModel.isLoading.value == false)
            assertThat(viewModel.userMessage.value?.peekContent(), `is`(R.string.all_error_unknown))
        }

    @Test
    fun fetchProducts_whenEmptyList_ReturnsEmptyProductsTrue() =
        testScope.runTest {
            // Given - an empty list
            fakeProductsRepository.setReturnEmpty(true)
            // When _ fetchProducts is called
            viewModel.fetchProducts()

            // Execute pending coroutines actions
            advanceUntilIdle()
            assert(viewModel.isEmptyProducts.value == true)
            assert(viewModel.isLoading.value == false)
        }

    @Test
    fun `setItemProductSelected should update productItem`() =
        runTest {
            // When
            viewModel.setItemProductSelected(1)
            // Execute pending coroutines actions
            advanceUntilIdle()
            // Then
            assertThat(viewModel.productItem.value, `is`(fakeProductsRepository.fakeProducts.first()))
        }
}
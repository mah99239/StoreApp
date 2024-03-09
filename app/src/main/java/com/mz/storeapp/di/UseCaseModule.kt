package com.mz.storeapp.di

import com.mz.storeapp.data.repository.ProductsRepository
import com.mz.storeapp.domain.usecases.GetAllProductsUseCase
import com.mz.storeapp.domain.usecases.GetProductItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger module that provides use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    /**
     * Provides a GetProductItemUseCase instance.
     *
     * @param productRepository The product repository.
     * @return A GetProductItemUseCase instance.
     */
    @Provides
    @Singleton
    fun provideGetProductsUseCase(productRepository: ProductsRepository) = GetProductItemUseCase(productRepository)

    /**
     * Provides a GetAllProductsUseCase instance.
     *
     * @param productRepository The product repository.
     * @return A GetAllProductsUseCase instance.
     */
    @Provides
    @Singleton
    fun provideGetAllProductsUseCase(productRepository: ProductsRepository) = GetAllProductsUseCase(productRepository)
}
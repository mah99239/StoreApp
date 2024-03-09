package com.mz.storeapp.di

import com.mz.storeapp.data.repository.ProductsRepository
import com.mz.storeapp.data.repository.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * A Dagger module that provides bindings for the repository.
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    /**
     * Binds the `ProductsRepositoryImpl` to the `ProductsRepository` interface.
     *
     * @param repositoryImpl The implementation of the `ProductsRepository` interface.
     * @return The bound `ProductsRepository` interface.
     */
    @Binds
    fun bindRepository(repositoryImpl: ProductsRepositoryImpl): ProductsRepository
}
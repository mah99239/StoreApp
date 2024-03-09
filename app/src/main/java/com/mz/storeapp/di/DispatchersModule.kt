package com.mz.storeapp.di

import com.mz.storeapp.di.utils.AppDispatchers
import com.mz.storeapp.di.utils.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * A Dagger module that provides coroutine dispatchers.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    /**
     * Provides the default coroutine dispatcher.
     *
     * @return The default coroutine dispatcher.
     */
    @Dispatcher(AppDispatchers.DEFAULT)
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    /**
     * Provides the IO coroutine dispatcher.
     *
     * @return The IO coroutine dispatcher.
     */
    @Dispatcher(AppDispatchers.IO)
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
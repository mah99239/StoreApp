package com.mz.storeapp.di

import com.mz.storeapp.BuildConfig
import com.mz.storeapp.data.remote.ProductsNetworkDataSource
import com.mz.storeapp.data.remote.RetrofitProductsNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * A Dagger module that provides network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provides a Gson instance for JSON serialization and deserialization.
     *
     * @return A Gson instance.
     */
    @Provides
    @Singleton
    fun providesNetworkJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    /**
     * Provides an OkHttpClient instance for making HTTP requests.
     *
     * @return An OkHttpClient instance.
     */
    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .build()
}

/**
 * A Dagger module that binds network-related interfaces to their implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindingModule {
    /**
     * Binds the `RetrofitProductsNetworkDataSource` implementation to the `ProductsNetworkDataSource` interface.
     *
     * @param impl The `RetrofitProductsNetworkDataSource` instance.
     * @return The `ProductsNetworkDataSource` instance.
     */
    @Binds
    abstract fun bindsProductsNetworkDataSource(impl: RetrofitProductsNetworkDataSource): ProductsNetworkDataSource
}
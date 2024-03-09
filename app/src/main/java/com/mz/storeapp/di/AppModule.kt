package com.mz.storeapp.di

import android.content.Context
import com.mz.storeapp.data.utils.ConnectivityManagerNetworkMonitor
import com.mz.storeapp.data.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger module that provides dependencies for the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides a [NetworkMonitor] instance.
     *
     * @param context The application context.
     * @return A [NetworkMonitor] instance.
     */
    @Provides
    @Singleton
    fun provideConnectivityManagerNetworkMonitor(
        @ApplicationContext context: Context,
    ): NetworkMonitor {
        return ConnectivityManagerNetworkMonitor(context)
    }
}
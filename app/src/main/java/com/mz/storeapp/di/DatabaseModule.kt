package com.mz.storeapp.di

import android.content.Context
import androidx.room.Room
import com.mz.storeapp.data.db.AppDatabase
import com.mz.storeapp.data.db.ProductsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger module that provides dependencies related to the database.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Provides an instance of the AppDatabase.
     *
     * @param context The application context.
     * @return An instance of the AppDatabase.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "store-db",
        ).build()

    /**
     * Provides an instance of the ProductsDao.
     *
     * @param database The AppDatabase instance.
     * @return An instance of the ProductsDao.
     */
    @Provides
    @Singleton
    fun provideProductsDao(database: AppDatabase): ProductsDao = database.ProductsDao()
}
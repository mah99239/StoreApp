package com.mz.storeapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mz.storeapp.data.model.db.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductsDao(): ProductsDao
}
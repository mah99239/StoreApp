package com.mz.storeapp.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val description: String,
    val image: String,
    val price: Double,
    @ColumnInfo(name = "rating_rate")
    val ratingRate: Double,
    @ColumnInfo(name = "rating_count")
    val ratingCount: Int,
    val title: String
)
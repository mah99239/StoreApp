package com.mz.storeapp.data.model.remote

import kotlinx.serialization.Serializable

/**
 * Network representation of [ProductItem]
 */
@Serializable
data class ProductItem(
    val id: Int,
    val category: String,
    val description: String,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
)
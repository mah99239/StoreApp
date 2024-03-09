package com.mz.storeapp.data.mapper

import com.mz.storeapp.data.model.db.ProductEntity
import com.mz.storeapp.data.model.remote.ProductItem
import com.mz.storeapp.data.model.remote.Rating

/**
 * Converts the network model to the local model for persisting
 * by the local data source
 */
fun ProductItem.asEntity() = ProductEntity(
    id = id,
    category = category,
    description = description,
    image = image,
    price = price,
    ratingRate = rating.rate,
    ratingCount = rating.count,
    title = title
)

fun ProductEntity.asExternalModel() = ProductItem(
    id = id,
    category = category,
    description = description,
    image = image,
    price = price,
    rating = Rating(
        ratingCount, ratingRate
    ),
    title = title
)
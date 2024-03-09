package com.mz.storeapp.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val count: Int,
    val rate: Double,
)
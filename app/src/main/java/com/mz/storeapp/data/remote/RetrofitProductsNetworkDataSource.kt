package com.mz.storeapp.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mz.storeapp.data.model.remote.ProductItem
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit service object for creating [ProductsApi] calls
 */
private interface ProductsApi {
    /**
     * Get a list of products
     *
     * @return A list of [ProductItem] objects
     */
    @GET("products")
    suspend fun getProducts(): List<ProductItem>

    /**
     * Get a product by its ID
     *
     * @param productId The ID of the product to get
     * @return A [ProductItem] object
     */
    @GET("products")
    suspend fun getProduct(
        @Query("id") productId: Int,
    ): ProductItem
}

private const val BASE_URL = "https://fakestoreapi.com/"

@Singleton
class RetrofitProductsNetworkDataSource
    @Inject
    constructor(
        networkJson: Json,
        okhttpCallFactory: Call.Factory,
    ) : ProductsNetworkDataSource {
        /**
         * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
         */
        private val networkApi =
            Retrofit.Builder()
                .callFactory(okhttpCallFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .baseUrl(BASE_URL)
                .build()
                .create(ProductsApi::class.java)

        override suspend fun getProducts(): List<ProductItem> = networkApi.getProducts()

        override suspend fun getProduct(productId: Int): ProductItem = networkApi.getProduct(productId)
    }
package com.mz.storeapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mz.storeapp.data.model.db.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [ProductEntity]
 */
@Dao
interface ProductsDao {
    /**
     * Fetches product that matches the query parameter.
     */
    @Transaction
    @Query("select * from products")
    fun getProductsEntities(): List<ProductEntity>

    /**
     * Fetches product that matches id in the query parameter.
     */
    @Query("select * from products where id = :idProduct")
     fun getProductEntity(idProduct: Int): Flow<ProductEntity>

    /**
     * inserts [entities] into the db if they don't exist, and ignore those that do.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProducts(entities: List<ProductEntity>)
}
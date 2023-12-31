package id.allana.titipbarangku.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.DepositWithStore
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.data.model.StoreModel

@Dao
interface ConsignmentDao {
    /**
     * INSERT
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeposit(deposit: DepositModel): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductInDeposit(productDeposit: ProductDepositModel)

    /**
     * UPDATE
     */
    @Update
    suspend fun updateCategory(category: CategoryModel)
    @Update
    suspend fun updateStore(store: StoreModel)
    @Update
    suspend fun updateProduct(product: ProductModel)
    @Update
    suspend fun updateDeposit(deposit: DepositModel)
    @Update
    suspend fun updateProductDeposit(productDeposit: ProductDepositModel)

    /**
     * READ
     */
    @Query("SELECT * FROM store_model ORDER BY id_store ASC")
    fun getAllStore(): LiveData<List<StoreModel>>
    @Query("SELECT * FROM category_model ORDER BY id_category ASC")
    fun getAllCategory(): LiveData<List<CategoryModel>>
    @Transaction
    @Query("SELECT * FROM product_model")
    fun getCategoryWithProduct(): LiveData<List<ProductWithCategory>>
    @Query("SELECT * FROM product_model")
    fun getAllProduct(): LiveData<List<ProductModel>>
    @Transaction
    @Query("SELECT * FROM product_in_deposit_model WHERE id_deposit= :idDeposit")
    fun getAllProductInDeposit(idDeposit: Int): LiveData<List<ProductDepositWithProduct>>
    @Transaction
    @Query("SELECT * FROM deposit_model")
    fun getAllDepositWithStore(): LiveData<List<DepositWithStore>>
    @Transaction
    @Query("SELECT * FROM deposit_model WHERE status = 'DEPOSIT'")
    fun getAllUnfinishedDeposit(): LiveData<List<DepositWithStore>>
    @Query("SELECT * FROM deposit_model WHERE id_store= :idStore")
    fun getAllDepositInStore(idStore: Int): LiveData<List<DepositModel>>
    @Query("SELECT * FROM deposit_model")
    fun getAllDeposit(): LiveData<List<DepositModel>>
    @Transaction
    @Query("SELECT * FROM product_in_deposit_model")
    fun getAllProductInDeposit(): LiveData<List<ProductDepositWithProduct>>

    /**
     * DELETE
     */
    @Delete
    suspend fun deleteStore(store: StoreModel)
    @Delete
    suspend fun deleteCategory(category: CategoryModel)
    @Delete
    suspend fun deleteProduct(product: ProductModel)
    @Delete
    suspend fun deleteProductDeposit(productDeposit: ProductDepositModel)
    @Delete
    suspend fun deleteDeposit(deposit: DepositModel)


}
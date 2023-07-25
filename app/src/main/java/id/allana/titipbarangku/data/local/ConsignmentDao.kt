package id.allana.titipbarangku.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.data.model.StoreModel

@Dao
interface ConsignmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryModel)
    @Update
    suspend fun updateCategory(category: CategoryModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreModel)
    @Update
    suspend fun updateStore(store: StoreModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductModel)
    @Update
    suspend fun updateProduct(product: ProductModel)
    @Query("SELECT * FROM store_model ORDER BY id_store ASC")
    fun getAllStore(): LiveData<List<StoreModel>>
    @Query("SELECT * FROM category_model ORDER BY id_category ASC")
    fun getAllCategory(): LiveData<List<CategoryModel>>
    @Transaction
    @Query("SELECT * FROM product_model")
    fun getCategoryWithProduct(): LiveData<List<ProductWithCategory>>
    @Delete
    suspend fun deleteStore(store: StoreModel)
    @Delete
    suspend fun deleteCategory(category: CategoryModel)
    @Delete
    suspend fun deleteProduct(product: ProductModel)



}
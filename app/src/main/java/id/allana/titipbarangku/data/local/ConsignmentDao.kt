package id.allana.titipbarangku.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.StoreModel

@Dao
interface ConsignmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductModel)
    @Query("SELECT * FROM store_model ORDER BY id ASC")
    fun getAllStore(): LiveData<List<StoreModel>>

    @Query("SELECT * FROM category_model ORDER BY id ASC")
    fun getAllCategory(): LiveData<List<CategoryModel>>

    @Transaction
    @Query("SELECT * FROM product_model")
    fun getCategoryWithProduct(): LiveData<List<ProductWithCategory>>

    @Delete
    suspend fun deleteStore(store: StoreModel)

    @Delete
    suspend fun deleteCategory(category: CategoryModel)



}
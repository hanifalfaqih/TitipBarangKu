package id.allana.titipbarangku.data.repository

import androidx.lifecycle.LiveData
import id.allana.titipbarangku.data.local.ConsignmentDao
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.data.model.StoreModel

class ConsignmentRepository(private val consignmentDao: ConsignmentDao) {
    /**
     * INSERT
     */
    suspend fun insertCategory(category: CategoryModel) {
        consignmentDao.insertCategory(category)
    }
    suspend fun insertProduct(product: ProductModel) {
        consignmentDao.insertProduct(product)
    }
    suspend fun insertStore(store: StoreModel) {
        consignmentDao.insertStore(store)
    }
    suspend fun insertDeposit(deposit: DepositModel) {
        consignmentDao.insertDeposit(deposit)
    }

    /**
     * UPDATE
     */
    suspend fun updateCategory(category: CategoryModel) {
        consignmentDao.updateCategory(category)
    }
    suspend fun updateStore(storeModel: StoreModel) {
        consignmentDao.updateStore(storeModel)
    }
    suspend fun updateProduct(product: ProductModel) {
        consignmentDao.updateProduct(product)
    }

    /**
     * DELETE
     */
    suspend fun deleteStore(store: StoreModel) {
        consignmentDao.deleteStore(store)
    }
    suspend fun deleteCategory(category: CategoryModel) {
        consignmentDao.deleteCategory(category)
    }
    suspend fun deleteProduct(product: ProductModel) {
        consignmentDao.deleteProduct(product)
    }

    /**
     * READ
     */
    fun getAllStore(): LiveData<List<StoreModel>> = consignmentDao.getAllStore()
    fun getAllCategory(): LiveData<List<CategoryModel>> = consignmentDao.getAllCategory()
    fun getCategoryWithProduct(): LiveData<List<ProductWithCategory>> = consignmentDao.getCategoryWithProduct()
}
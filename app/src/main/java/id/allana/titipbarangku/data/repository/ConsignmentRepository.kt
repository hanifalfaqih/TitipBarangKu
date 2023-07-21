package id.allana.titipbarangku.data.repository

import androidx.lifecycle.LiveData
import id.allana.titipbarangku.data.local.ConsignmentDao
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.StoreModel

class ConsignmentRepository(private val consignmentDao: ConsignmentDao) {
    suspend fun insertCategory(category: CategoryModel) {
        consignmentDao.insertCategory(category)
    }

    suspend fun insertStore(store: StoreModel) {
        consignmentDao.insertStore(store)
    }

    suspend fun insertProduct(product: ProductModel) {
        consignmentDao.insertProduct(product)
    }

    suspend fun deleteStore(store: StoreModel) {
        consignmentDao.deleteStore(store)
    }

    suspend fun deleteCategory(category: CategoryModel) {
        consignmentDao.deleteCategory(category)
    }

    fun getAllStore(): LiveData<List<StoreModel>> = consignmentDao.getAllStore()
    fun getAllCategory(): LiveData<List<CategoryModel>> = consignmentDao.getAllCategory()

    fun getCategoryWithProduct(): LiveData<List<ProductWithCategory>> = consignmentDao.getCategoryWithProduct()
}
package id.allana.titipbarangku.data.repository

import id.allana.titipbarangku.data.local.ConsignmentDao
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.StoreModel

class ConsignmentRepository(private val consignmentDao: ConsignmentDao) {
    suspend fun insertCategory(category: CategoryModel) {
        consignmentDao.insertCategory(category)
    }

    suspend fun insertStore(store: StoreModel) {
        consignmentDao.insertStore(store)
    }
}
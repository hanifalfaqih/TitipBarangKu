package id.allana.titipbarangku.data.repository

import id.allana.titipbarangku.data.local.ConsignmentDao
import id.allana.titipbarangku.data.model.CategoryModel

class ConsignmentRepository(private val consignmentDao: ConsignmentDao) {

    suspend fun insertCategory(category: CategoryModel) {
        consignmentDao.insertCategory(category)
    }
}
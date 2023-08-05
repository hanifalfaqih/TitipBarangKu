package id.allana.titipbarangku.data.repository

import androidx.lifecycle.LiveData
import id.allana.titipbarangku.data.local.ConsignmentDao
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.DepositWithStore
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.util.convertToDouble
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
    suspend fun insertDeposit(deposit: DepositModel): Long {
        return consignmentDao.insertDeposit(deposit)
    }
    suspend fun insertProductInDeposit(productDeposit: ProductDepositModel) {
        return consignmentDao.insertProductInDeposit(productDeposit)
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
    suspend fun updateDeposit(deposit: DepositModel) {
        consignmentDao.updateDeposit(deposit)
    }
    suspend fun updateProductDeposit(productDeposit: ProductDepositModel) {
        consignmentDao.updateProductDeposit(productDeposit)
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
    suspend fun deleteProductDeposit(productDeposit: ProductDepositModel) {
        consignmentDao.deleteProductDeposit(productDeposit)
    }
    suspend fun deleteDeposit(deposit: DepositModel) {
        consignmentDao.deleteDeposit(deposit)
    }

    /**
     * READ
     */
    fun getAllStore(): LiveData<List<StoreModel>> = consignmentDao.getAllStore()
    fun getAllCategory(): LiveData<List<CategoryModel>> = consignmentDao.getAllCategory()
    fun getCategoryWithProduct(): LiveData<List<ProductWithCategory>> = consignmentDao.getCategoryWithProduct()
    fun getAllProduct(): LiveData<List<ProductModel>> = consignmentDao.getAllProduct()
    fun getAllProductInDeposit(idProduct: Int): LiveData<List<ProductDepositWithProduct>> = consignmentDao.getAllProductInDeposit(idProduct)
    fun getAllDepositWithStore(): LiveData<List<DepositWithStore>> = consignmentDao.getAllDepositWithStore()
    fun getAllDepositInStore(idStore: Int): LiveData<List<DepositModel>> = consignmentDao.getAllDepositInStore(idStore)
    fun getAllDeposit(): LiveData<List<DepositModel>> = consignmentDao.getAllDeposit()
    fun getAllProductInDeposit(): LiveData<List<ProductDepositWithProduct>> = consignmentDao.getAllProductInDeposit()
    fun filterByCurrentMonth(listDeposit: List<DepositModel>, listProductDeposit: List<ProductDepositWithProduct>): List<ProductDepositWithProduct> {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        val currentListDeposit = listDeposit.filter { deposit ->
            val depositStartDate = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).parse(deposit.startDateDeposit)
            val depositMonth = Calendar.getInstance().apply {
                time = depositStartDate as Date
            }.get(Calendar.MONTH)
            depositMonth == currentMonth
        }

        return listProductDeposit.filter { productDeposit ->
            currentListDeposit.any { it.id == productDeposit.productDeposit.idDeposit }
        }
    }

    fun calculateTotalProductSoldWithPrice(listProductDeposit: List<ProductDepositWithProduct>): Double {
        var totalAmount = 0.0
        for (productDeposit in listProductDeposit) {
            val productPrice = productDeposit.product?.price?.let { convertToDouble(it) }
            val totalProductSold = productDeposit.productDeposit.totalProductSold.toDouble()
            totalAmount =  totalProductSold * productPrice!!
        }
        return totalAmount
    }
}
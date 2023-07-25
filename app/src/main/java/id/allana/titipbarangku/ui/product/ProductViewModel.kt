package id.allana.titipbarangku.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.ConsignmentDatabase
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)

    private val checkDatabaseEmptyLiveData = MutableLiveData<Boolean>()

    fun checkDatabaseEmpty(data: List<ProductWithCategory>) {
        checkDatabaseEmptyLiveData.value = data.isEmpty()
    }
    fun checkDatabaseEmptyLiveData(): LiveData<Boolean> = checkDatabaseEmptyLiveData
    fun insertProduct(product: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(product)
        }
    }
    fun updateProduct(product: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }

    fun getCategoryWithProduct(): LiveData<List<ProductWithCategory>> = repository.getCategoryWithProduct()
}
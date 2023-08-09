package id.allana.titipbarangku.ui.deposit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.room.ConsignmentDatabase
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.DepositWithStore
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.data.model.Status
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)

    private val _idDeposit = MutableLiveData<Long>()
    val idDeposit: LiveData<Long> = _idDeposit

    private val updateStatusDeposit = MutableLiveData<Status>()
    private val checkDatabaseEmpty = MutableLiveData<Boolean>()

    fun insertDeposit(deposit: DepositModel) {
        viewModelScope.launch {
            val depositId = repository.insertDeposit(deposit)
            _idDeposit.value = depositId
        }
    }

    fun insertProductInDeposit(productDeposit: ProductDepositModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProductInDeposit(productDeposit)
        }
    }

    fun updateDeposit(deposit: DepositModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDeposit(deposit)
        }
    }

    fun updateProductDeposit(productDeposit: ProductDepositModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProductDeposit(productDeposit)
        }
    }

    fun deleteProductDeposit(productDeposit: ProductDepositModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProductDeposit(productDeposit)
        }
    }

    fun deleteDeposit(deposit: DepositModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDeposit(deposit)
        }
    }

    fun getAllProductInDeposit(idDeposit: Int) = repository.getAllProductInDeposit(idDeposit)
    fun getAllDepositWithStore() = repository.getAllDepositWithStore()

    fun getUpdateStatusDeposit(data: Status) {
        updateStatusDeposit.value = data
    }

    fun getUpdateStatusDepositLiveData(): LiveData<Status> = updateStatusDeposit

    fun checkProductInDeposit(data: List<ProductDepositWithProduct>) {
        checkDatabaseEmpty.value = data.isEmpty()
    }
    fun checkProductInDepositLiveData(): LiveData<Boolean> = checkDatabaseEmpty

    fun checkDeposit(data: List<DepositWithStore>) {
        checkDatabaseEmpty.value = data.isEmpty()
    }
    fun checkDepositLiveData(): LiveData<Boolean> = checkDatabaseEmpty

}
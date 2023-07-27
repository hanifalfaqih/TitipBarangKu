package id.allana.titipbarangku.ui.deposit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.ConsignmentDatabase
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.ProductInDepositModel
import id.allana.titipbarangku.data.model.ProductInDepositWithProductModel
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)

    private val _idDeposit = MutableLiveData<Long>()
    val idDeposit: LiveData<Long> = _idDeposit

    private val checkDatabaseEmptyLiveData = MutableLiveData<Boolean>()

    fun insertDeposit(deposit: DepositModel) {
        viewModelScope.launch {
            val depositId = repository.insertDeposit(deposit)
            _idDeposit.value = depositId
        }
    }

    fun insertProductInDeposit(productDeposit: ProductInDepositModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProductInDeposit(productDeposit)
        }
    }

    fun getAllProductInDeposit(idDeposit: Int) = repository.getAllProductInDeposit(idDeposit)

    fun checkDatabaseEmpty(data: List<ProductInDepositWithProductModel>) {
        checkDatabaseEmptyLiveData.value = data.isEmpty()
    }
    fun checkDatabaseEmptyLiveData(): LiveData<Boolean> = checkDatabaseEmptyLiveData

}
package id.allana.titipbarangku.ui.homepage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.ConsignmentDatabase
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.launch

class HomepageViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)

    private val getAllFilteredDepositLiveData = MutableLiveData<List<ProductDepositWithProduct>>()
    private val calculateTotalAmountLiveData = MutableLiveData<String>()

    fun getAllUnfinishedDeposit() = repository.getAllUnfinishedDeposit()
    fun getAllDeposit() = repository.getAllDeposit()
    fun getAllProductInDeposit() = repository.getAllProductInDeposit()

    fun getAllFilteredDeposit(listDeposit: List<DepositModel>, listProductDeposit: List<ProductDepositWithProduct>) {
        viewModelScope.launch {
            getAllFilteredDepositLiveData.postValue(repository.filterByCurrentMonth(listDeposit, listProductDeposit))
        }
    }

    fun calculateTotalAmountLiveData(listProductDeposit: List<ProductDepositWithProduct>) {
        viewModelScope.launch {
            calculateTotalAmountLiveData.postValue(repository.calculateTotalProductSoldWithPrice(listProductDeposit))
        }
    }

    fun getAllFilterdDepositLiveData(): LiveData<List<ProductDepositWithProduct>> = getAllFilteredDepositLiveData
    fun calculateTotalAmountLiveData(): LiveData<String> = calculateTotalAmountLiveData
}
package id.allana.titipbarangku.ui.homepage

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.datastore.UserPreferences
import id.allana.titipbarangku.data.local.datastore.dataStore
import id.allana.titipbarangku.data.local.room.ConsignmentDatabase
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.DepositWithStore
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import id.allana.titipbarangku.data.repository.UserPreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomepageViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)
    private val userPreference = UserPreferences.getInstance(application.dataStore)
    private val userPrefRepository = UserPreferenceRepository(userPreference)

    private val filteredDepositLiveData = MutableLiveData<List<ProductDepositWithProduct>>()
    private val filteredDepositEachMonthLiveData = MutableLiveData<Map<Int, List<ProductDepositWithProduct>>>()
    private val totalAmountLiveData = MutableLiveData<Int>()
    private val totalAmountEachMonthLiveData = MutableLiveData<Map<Int, Int>>()
    private val checkDatabaseEmptyLiveData = MutableLiveData<Boolean>()

    private val authUserLiveData = MutableLiveData<String>()

    fun checkDatabaseEmpty(data: List<DepositWithStore>) {
        checkDatabaseEmptyLiveData.value = data.isEmpty()
    }
    fun checkDatabaseEmptyLiveData(): LiveData<Boolean> = checkDatabaseEmptyLiveData
    fun getAllUnfinishedDeposit() = repository.getAllUnfinishedDeposit()
    fun getAllDeposit() = repository.getAllDeposit()
    fun getAllProductInDeposit() = repository.getAllProductInDeposit()
    fun getAllFilteredDeposit(listDeposit: List<DepositModel>, listProductDeposit: List<ProductDepositWithProduct>) {
        viewModelScope.launch {
            filteredDepositLiveData.postValue(repository.filterByCurrentMonth(listDeposit, listProductDeposit))
        }
    }
    fun getAllFilteredDepositEachMonth(listDeposit: List<DepositModel>, listProductDeposit: List<ProductDepositWithProduct>) {
        viewModelScope.launch {
            filteredDepositEachMonthLiveData.postValue(repository.filterByEachMonth(listDeposit, listProductDeposit))
        }
    }
    fun calculateTotalAmount(listProductDeposit: List<ProductDepositWithProduct>) {
        viewModelScope.launch {
            val totalAmount = repository.calculateTotalProductSoldWithPrice(listProductDeposit)
            totalAmountLiveData.postValue(totalAmount)
        }
    }
    fun calculateTotalAmountEachMonth(listProductDeposit: Map<Int, List<ProductDepositWithProduct>>) {
        viewModelScope.launch {
            val totalAmount = repository.calculateTotalProductSoldWithPriceEachMonth(listProductDeposit)
            totalAmountEachMonthLiveData.postValue(totalAmount)
        }
    }
    fun getAllFilterdDepositLiveData(): LiveData<List<ProductDepositWithProduct>> = filteredDepositLiveData
    fun getAllFilteredDepositEachMonthLiveData(): LiveData<Map<Int, List<ProductDepositWithProduct>>> = filteredDepositEachMonthLiveData
    fun calculateTotalAmountLiveData(): LiveData<Int> = totalAmountLiveData
    fun calculateTotalAmountEachMonthLiveData(): LiveData<Map<Int, Int>> = totalAmountEachMonthLiveData

    /**
     * DATA STORE
     */
    fun setUserFirstTimeOpenApp(isFirstTimeOpen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefRepository.setUserFirstTimeOpenApp(isFirstTimeOpen)
        }
    }
    fun getUserFirstTimeOpenApp(): LiveData<Boolean> {
        return userPrefRepository.getUserFirstTimeOpenApp().asLiveData()
    }
    fun setUserStoreName(userStoreName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefRepository.setUserStoreName(userStoreName)
        }
    }
    fun getUserStoreName(): LiveData<String> {
        return userPrefRepository.getUserStoreName().asLiveData()
    }
    fun setAuthUser(auth: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefRepository.setAuthUser(auth, context)
        }
    }
    fun getAuthUser(context: Context) {
        val authUser = userPrefRepository.getAuthUser(context)
        authUserLiveData.value = authUser
    }
    fun getAuthUserLiveData(): LiveData<String> = authUserLiveData

    fun getAppLanguangeLiveData(): LiveData<String> {
        return userPrefRepository.getAppLanguage().asLiveData()
    }
    fun setAppLanguage(language: String) {
        viewModelScope.launch {
            userPrefRepository.setAppLanguage(language)
        }
    }

    fun getLastDateBackup(): LiveData<String> {
        return userPrefRepository.getLastDateBackup().asLiveData()
    }
    fun setLastDateBackup(date: String) {
        viewModelScope.launch {
            userPrefRepository.setLastDateBackup(date)
        }
    }

}
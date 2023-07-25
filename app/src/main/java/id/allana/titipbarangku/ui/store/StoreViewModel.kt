package id.allana.titipbarangku.ui.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.ConsignmentDatabase
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)

    private val checkDatabaseEmptyLiveData = MutableLiveData<Boolean>()
    fun checkDatabaseEmpty(data: List<StoreModel>) {
        checkDatabaseEmptyLiveData.value = data.isEmpty()
    }

    fun checkDatabaseEmptyLiveData(): LiveData<Boolean> = checkDatabaseEmptyLiveData

    fun insertStore(store: StoreModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertStore(store)
        }
    }
    fun updateStore(store: StoreModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStore(store)
        }
    }

    fun deleteStore(store: StoreModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStore(store)
        }
    }

    fun getAllStore(): LiveData<List<StoreModel>> = repository.getAllStore()

}
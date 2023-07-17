package id.allana.titipbarangku.ui.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.ConsignmentDatabase
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)

    fun insertStore(store: StoreModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertStore(store)
        }
    }

}
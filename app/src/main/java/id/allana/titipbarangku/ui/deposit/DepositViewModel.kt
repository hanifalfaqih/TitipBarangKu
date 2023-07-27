package id.allana.titipbarangku.ui.deposit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.ConsignmentDatabase
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val repository = ConsignmentRepository(consignmentDao)

    fun insertDeposit(deposit: DepositModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDeposit(deposit)
        }
    }
}
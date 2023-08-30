package id.allana.titipbarangku.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.allana.titipbarangku.data.local.room.ConsignmentDatabase
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.Status
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.data.model.firebasemodel.CategoryModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.DepositModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.ProductDepositModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.ProductModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.StoreModelFirebase
import id.allana.titipbarangku.data.repository.BackupRestoreRepository
import id.allana.titipbarangku.data.repository.ConsignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {

    private val consignmentDao = ConsignmentDatabase.getDatabase(application).consignmentDao()
    private val backupRestoreRepository = BackupRestoreRepository(consignmentDao)
    private val consignmentRepository = ConsignmentRepository(consignmentDao)

    /**
     * DELETE ALL
     */
    fun deleteAllCategories(listCategories: List<CategoryModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            consignmentRepository.deleteAllCategories(listCategories)
        }
    }
    fun deleteAllProducts(listProducts: List<ProductModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            consignmentRepository.deleteAllProducts(listProducts)
        }
    }
    fun deleteAllDeposits(listDeposits: List<DepositModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            consignmentRepository.deleteAllDeposits(listDeposits)
        }
    }
    fun deleteAllStores(listStores: List<StoreModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            consignmentRepository.deleteAllStores(listStores)
        }
    }
    fun deleteAllProductDeposits(listProductDeposits: List<ProductDepositModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            consignmentRepository.deleteAllProductDeposits(listProductDeposits)
        }
    }

    /**
     * BACKUP TO FIREBASE
     */
    fun backupCategoryToFirebase(listCategory: List<CategoryModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            backupRestoreRepository.insertCategoryToFirebase(listCategory, callback)
        }
    }
    fun backupProductToFirebase(listProduct: List<ProductModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            backupRestoreRepository.insertProductToFirebase(listProduct, callback)
        }
    }
    fun backupDepositToFirebase(listDeposit: List<DepositModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            backupRestoreRepository.insertDepositToFirebase(listDeposit, callback)
        }
    }
    fun backupStoreToFirebase(listStore: List<StoreModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            backupRestoreRepository.insertStoreToFirebase(listStore, callback)
        }
    }
    fun backupProductDepositToFirebase(listProductDeposit: List<ProductDepositModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            backupRestoreRepository.insertProductDepositToFirebase(listProductDeposit, callback)
        }
    }

    /**
     * RESTORE FROM FIREBASE
     */
    fun restoreCategoryFromFirebase(): LiveData<List<CategoryModelFirebase>> {
        val resultLiveData = MutableLiveData<List<CategoryModelFirebase>>()

        viewModelScope.launch {
            val list = backupRestoreRepository.getCategoryFromFirebase()
            resultLiveData.value = list
        }
        return resultLiveData
    }
    fun convertToCategoryModel(categoryModelFirebase: CategoryModelFirebase): CategoryModel {
        return CategoryModel(categoryModelFirebase.id, categoryModelFirebase.categoryName)
    }
    fun restoreProductFromFirebase(): LiveData<List<ProductModelFirebase>> {
        val resultLiveData = MutableLiveData<List<ProductModelFirebase>>()

        viewModelScope.launch {
            val list = backupRestoreRepository.getProductFromFirebase()
            resultLiveData.value = list
        }
        return resultLiveData
    }
    fun convertToProductModel(productModelFirebase: ProductModelFirebase): ProductModel {
        return ProductModel(productModelFirebase.id, productModelFirebase.idCategory, productModelFirebase.name, productModelFirebase.price)
    }
    fun restoreDepositFromFirebase(): LiveData<List<DepositModelFirebase>> {
        val resultLiveData = MutableLiveData<List<DepositModelFirebase>>()

        viewModelScope.launch {
            val list = backupRestoreRepository.getDepositFromFirebase()
            resultLiveData.value = list
        }
        return resultLiveData
    }
    fun convertToDepositModel(depositModelFirebase: DepositModelFirebase): DepositModel {
        return DepositModel(depositModelFirebase.id, depositModelFirebase.idStore, depositModelFirebase.startDateDeposit, depositModelFirebase.finishDateDeposit, Status.valueOf(depositModelFirebase.status))
    }
    fun restoreStoreFromFirebase(): LiveData<List<StoreModelFirebase>> {
        val resultLiveData = MutableLiveData<List<StoreModelFirebase>>()

        viewModelScope.launch {
            val list = backupRestoreRepository.getStoreFromFirebase()
            resultLiveData.value = list
        }
        return resultLiveData
    }
    fun convertToStoreModel(storeModelFirebase: StoreModelFirebase): StoreModel {
        return StoreModel(storeModelFirebase.id, storeModelFirebase.name, storeModelFirebase.address, storeModelFirebase.ownerName, storeModelFirebase.ownerPhoneNumber)
    }
    fun restoreProductDepositFromFirebase(): LiveData<List<ProductDepositModelFirebase>> {
        val resultLiveData = MutableLiveData<List<ProductDepositModelFirebase>>()

        viewModelScope.launch {
            val list = backupRestoreRepository.getProductDepositFromFirebase()
            resultLiveData.value = list
        }
        return resultLiveData
    }
    fun convertToProductDepositModel(productDepositModelFirebase: ProductDepositModelFirebase): ProductDepositModel {
        return ProductDepositModel(productDepositModelFirebase.id, productDepositModelFirebase.idDeposit, productDepositModelFirebase.idProduct, productDepositModelFirebase.quantity, productDepositModelFirebase.returnQuantity, productDepositModelFirebase.totalProductSold, productDepositModelFirebase.isExpanded)
    }

    /**
     * INSERT TO ROOM FROM FIREBASE
     */
    fun insertAllCategories(listCategories: List<CategoryModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreRepository.insertAllCategories(listCategories, callback)
        }
    }
    fun insertAllProducts(listProducts: List<ProductModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreRepository.insertAllProducts(listProducts, callback)
        }
    }
    fun insertAllDeposits(listDeposits: List<DepositModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreRepository.insertAllDeposits(listDeposits, callback)
        }
    }
    fun insertAllStores(listStores: List<StoreModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreRepository.insertAllStores(listStores, callback)
        }
    }
    fun insertAllProductDeposits(listProductDeposits: List<ProductDepositModel>, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreRepository.insertAllProductDeposits(listProductDeposits, callback)
        }
    }
}
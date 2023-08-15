package id.allana.titipbarangku.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.data.model.firebasemodel.CategoryModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.DepositModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.ProductDepositModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.ProductModelFirebase
import id.allana.titipbarangku.data.model.firebasemodel.StoreModelFirebase

class BackupRestoreRepository {

    private val authUid = Firebase.auth.uid.toString()
    private val insertDataCategory = Firebase.database.reference.child(authUid).child("categories")
    private val insertDataProduct = Firebase.database.reference.child(authUid).child("products")
    private val insertDataStore = Firebase.database.reference.child(authUid).child("stores")
    private val insertDataDeposit = Firebase.database.reference.child(authUid).child("deposits")
    private val insertDataProductDeposit = Firebase.database.reference.child(authUid).child("product_deposits")

    fun insertCategoryToFirebase(listCategory: List<CategoryModel>, callback: (Boolean) -> Unit) {
        val list = mutableListOf<CategoryModelFirebase>()

        for (category in listCategory) {
            val categoryModelFirebase = CategoryModelFirebase(
                id = category.id,
                categoryName = category.categoryName
            )
            list.add(categoryModelFirebase)
        }

        val gson = Gson()
        val jsonList = gson.toJson(list)

        insertDataCategory.setValue(jsonList).addOnSuccessListener { callback(true) }.addOnFailureListener { callback(false) }
        Log.d(BackupRestoreRepository::class.java.simpleName, list.toString())
    }

    fun insertProductToFirebase(listProduct: List<ProductModel>, callback: (Boolean) -> Unit) {
        val list = mutableListOf<ProductModelFirebase>()

        for (product in listProduct) {
            val productModelFirebase = ProductModelFirebase(
                id = product.id,
                idCategory = product.idCategory,
                name = product.name,
                price = product.price
            )
            list.add(productModelFirebase)
        }

        val gson = Gson()
        val jsonList = gson.toJson(list)

        insertDataProduct.setValue(jsonList).addOnSuccessListener { callback(true) }.addOnFailureListener { callback(false) }
    }

    fun insertStoreToFirebase(listStore: List<StoreModel>, callback: (Boolean) -> Unit) {
        val list = mutableListOf<StoreModelFirebase>()

        for (store in listStore) {
            val storeModelFirebase = StoreModelFirebase(
                id = store.id,
                name = store.name,
                address = store.address,
                ownerName = store.ownerName,
                ownerPhoneNumber = store.ownerPhoneNumber
            )
            list.add(storeModelFirebase)
        }

        val gson = Gson()
        val jsonList = gson.toJson(list)

        insertDataStore.setValue(jsonList).addOnSuccessListener { callback(true) }.addOnFailureListener { callback(false) }
    }

    fun insertDepositToFirebase(listDeposit: List<DepositModel>, callback: (Boolean) -> Unit) {
        val list = mutableListOf<DepositModelFirebase>()

        for (deposit in listDeposit) {
            val depositModelFirebase = DepositModelFirebase(
                id = deposit.id,
                idStore = deposit.idStore,
                startDateDeposit = deposit.startDateDeposit,
                finishDateDeposit = deposit.finishDateDeposit,
                status = deposit.status.toString()
            )
            list.add(depositModelFirebase)
        }

        val gson = Gson()
        val jsonList = gson.toJson(list)

        insertDataDeposit.setValue(jsonList).addOnSuccessListener { callback(true) }.addOnFailureListener { callback(false) }
    }

    fun insertProductDepositToFirebase(listProductDeposit: List<ProductDepositModel>, callback: (Boolean) -> Unit) {
        val list = mutableListOf<ProductDepositModelFirebase>()

        for (productDeposit in listProductDeposit) {
            val productDepositModelFirebase = ProductDepositModelFirebase(
                id = productDeposit.id,
                idDeposit = productDeposit.idDeposit,
                idProduct = productDeposit.idProduct,
                quantity = productDeposit.quantity,
                returnQuantity = productDeposit.returnQuantity,
                totalProductSold = productDeposit.totalProductSold,
                isExpanded = productDeposit.isExpanded
            )
            list.add(productDepositModelFirebase)
        }

        val gson = Gson()
        val jsonList = gson.toJson(list)

        insertDataProductDeposit.setValue(jsonList).addOnSuccessListener { callback(true) }.addOnFailureListener { callback(false) }
    }
}
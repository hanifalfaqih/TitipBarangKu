package id.allana.titipbarangku.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.allana.titipbarangku.data.local.room.ConsignmentDao
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BackupRestoreRepository(private val consignmentDao: ConsignmentDao) {

    private val authUid = Firebase.auth.uid.toString()
    private val insertDataCategory =
        Firebase.firestore.collection(authUid).document("category").collection("categories")
    private val insertDataProduct =
        Firebase.firestore.collection(authUid).document("product").collection("products")
    private val insertDataStore =
        Firebase.firestore.collection(authUid).document("store").collection("stores")
    private val insertDataDeposit =
        Firebase.firestore.collection(authUid).document("deposit").collection("deposits")
    private val insertDataProductDeposit =
        Firebase.firestore.collection(authUid).document("product_deposit")
            .collection("product_deposits")

    /**
     * BACKUP TO FIREBASE
     */
    fun insertCategoryToFirebase(listCategory: List<CategoryModel>, callback: (Boolean) -> Unit) {
        insertDataCategory.get().addOnCompleteListener { task ->
            /**
             * DELETE ALL DATA REFERENCE IN FIREBASE BEFORE BACKUP
             */
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d(
                            "DELETE CATEGORIES",
                            "Success -> DELETE LAST CATEGORIES AND INSERT NEW CATEGORIES"
                        )
                    }.addOnFailureListener {
                        Log.d(
                            "DELETE CATEGORIES",
                            "Failed -> DELETE LAST CATEGORIES AND INSERT NEW CATEGORIES"
                        )
                    }
                }
            }
            /**
             * INSERT ALL DATA IN FIREBASE
             */
            for (item in listCategory) {
                val category = CategoryModelFirebase(
                    id = item.id,
                    categoryName = item.categoryName
                )
                insertDataCategory.document(category.id.toString()).set(category.toMap())
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }
    }

    fun insertProductToFirebase(listProduct: List<ProductModel>, callback: (Boolean) -> Unit) {
        insertDataProduct.get().addOnCompleteListener { task ->
            /**
             * DELETE ALL DATA REFERENCE IN FIREBASE BEFORE BACKUP
             */
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d(
                            "DELETE PRODUCT",
                            "Success -> DELETE LAST PRODUCTS AND INSERT NEW PRODUCTS"
                        )
                    }.addOnFailureListener {
                        Log.d(
                            "DELETE PRODUCT",
                            "Failed -> DELETE LAST PRODUCTS AND INSERT NEW PRODUCTS"
                        )
                    }
                }
            }
            /**
             * INSERT ALL DATA IN FIREBASE
             */
            for (item in listProduct) {
                val product = ProductModelFirebase(
                    id = item.id,
                    idCategory = item.idCategory,
                    name = item.name,
                    price = item.price
                )
                insertDataProduct.document(product.id.toString()).set(product.toMap())
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }
    }

    fun insertStoreToFirebase(listStore: List<StoreModel>, callback: (Boolean) -> Unit) {
        insertDataStore.get().addOnCompleteListener { task ->
            /**
             * DELETE ALL DATA REFERENCE IN FIREBASE BEFORE BACKUP
             */
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d(
                            "DELETE STORE",
                            "Success -> DELETE LAST STORES AND INSERT NEW STORES"
                        )
                    }.addOnFailureListener {
                        Log.d(
                            "DELETE CATEGORIES",
                            "Failed -> DELETE LAST STORES AND INSERT NEW STORES"
                        )
                    }
                }
            }
            /**
             * INSERT ALL DATA IN FIREBASE
             */
            for (item in listStore) {
                val store = StoreModelFirebase(
                    id = item.id,
                    name = item.name,
                    address = item.address,
                    ownerName = item.ownerName,
                    ownerPhoneNumber = item.ownerPhoneNumber
                )
                insertDataStore.document(store.id.toString()).set(store.toMap())
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }
    }

    fun insertDepositToFirebase(listDeposit: List<DepositModel>, callback: (Boolean) -> Unit) {
        insertDataDeposit.get().addOnCompleteListener { task ->
            /**
             * DELETE ALL DATA REFERENCE IN FIREBASE BEFORE BACKUP
             */
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d(
                            "DELETE DEPOSIT",
                            "Success -> DELETE LAST DEPOSITS AND INSERT NEW DEPOSITS"
                        )
                    }.addOnFailureListener {
                        Log.d(
                            "DELETE DEPOSIT",
                            "Failed -> DELETE LAST DEPOSITS AND INSERT NEW DEPOSITS"
                        )
                    }
                }
            }
            /**
             * INSERT ALL DATA IN FIREBASE
             */
            for (item in listDeposit) {
                val deposit = DepositModelFirebase(
                    id = item.id,
                    idStore = item.idStore,
                    startDateDeposit = item.startDateDeposit,
                    finishDateDeposit = item.finishDateDeposit,
                    status = item.status.name
                )
                insertDataDeposit.document(deposit.id.toString()).set(deposit.toMap())
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }
    }

    fun insertProductDepositToFirebase(
        listProductDeposit: List<ProductDepositModel>,
        callback: (Boolean) -> Unit
    ) {
        insertDataProductDeposit.get().addOnCompleteListener { task ->
            /**
             * DELETE ALL DATA REFERENCE IN FIREBASE BEFORE BACKUP
             */
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d(
                            "DELETE PRODUCT DEPOSITS",
                            "Success -> DELETE LAST PRODUCT DEPOSITS AND INSERT NEW PRODUCT DEPOSITS"
                        )
                    }.addOnFailureListener {
                        Log.d(
                            "DELETE PRODUCT DEPOSITS",
                            "Failed -> ELETE LAST PRODUCT DEPOSITS AND INSERT NEW PRODUCT DEPOSITS"
                        )
                    }
                }
            }
            /**
             * INSERT ALL DATA IN FIREBASE
             */
            for (item in listProductDeposit) {
                val productDeposit = ProductDepositModelFirebase(
                    id = item.id,
                    idDeposit = item.idDeposit,
                    idProduct = item.idProduct,
                    quantity = item.quantity,
                    returnQuantity = item.returnQuantity,
                    totalProductSold = item.totalProductSold,
                    isExpanded = item.isExpanded
                )
                insertDataProductDeposit.document(productDeposit.id.toString())
                    .set(productDeposit.toMap()).addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }
    }

    /**
     * RESTORE FROM FIREBASE
     */
    suspend fun getCategoryFromFirebase(): List<CategoryModelFirebase> {
        return withContext(Dispatchers.IO) {
            return@withContext insertDataCategory.get().await()
                .toObjects(CategoryModelFirebase::class.java)
        }
    }

    suspend fun getProductFromFirebase(): List<ProductModelFirebase> {
        return withContext(Dispatchers.IO) {
            return@withContext insertDataProduct.get().await()
                .toObjects(ProductModelFirebase::class.java)
        }
    }

    suspend fun getDepositFromFirebase(): List<DepositModelFirebase> {
        return withContext(Dispatchers.IO) {
            return@withContext insertDataDeposit.get().await()
                .toObjects(DepositModelFirebase::class.java)
        }
    }

    suspend fun getStoreFromFirebase(): List<StoreModelFirebase> {
        return withContext(Dispatchers.IO) {
            return@withContext insertDataStore.get().await()
                .toObjects(StoreModelFirebase::class.java)
        }
    }

    suspend fun getProductDepositFromFirebase(): List<ProductDepositModelFirebase> {
        return withContext(Dispatchers.IO) {
            return@withContext insertDataProductDeposit.get().await()
                .toObjects(ProductDepositModelFirebase::class.java)
        }
    }

    /**
     * INSERT TO ROOM FROM FIREBASE
     */
    suspend fun insertAllCategories(
        listCategories: List<CategoryModel>,
        callback: (Boolean) -> Unit
    ) {
        val listRowId = consignmentDao.insertAllCategoriesFromFirebase(listCategories)
        if (listRowId.isNotEmpty()) callback(true) else callback(false)
    }

    suspend fun insertAllProducts(listProducts: List<ProductModel>, callback: (Boolean) -> Unit) {
        val listRowId = consignmentDao.insertAllProductsFromFirebase(listProducts)
        if (listRowId.isNotEmpty()) callback(true) else callback(false)
    }

    suspend fun insertAllDeposits(listDeposits: List<DepositModel>, callback: (Boolean) -> Unit) {
        val listRowId = consignmentDao.insertAllDepositsFromFirebase(listDeposits)
        if (listRowId.isNotEmpty()) callback(true) else callback(false)
    }

    suspend fun insertAllStores(listStores: List<StoreModel>, callback: (Boolean) -> Unit) {
        val listRowId = consignmentDao.insertAllStoresFromFirebase(listStores)
        if (listRowId.isNotEmpty()) callback(true) else callback(false)
    }

    suspend fun insertAllProductDeposits(
        listProductDeposits: List<ProductDepositModel>,
        callback: (Boolean) -> Unit
    ) {
        val listRowId = consignmentDao.insertAllProductDepositsFromFirebase(listProductDeposits)
        if (listRowId.isNotEmpty()) callback(true) else callback(false)
    }

}


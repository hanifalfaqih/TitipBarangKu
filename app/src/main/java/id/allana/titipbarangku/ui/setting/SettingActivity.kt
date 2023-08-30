package id.allana.titipbarangku.ui.setting

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseActivity
import id.allana.titipbarangku.databinding.ActivitySettingBinding
import id.allana.titipbarangku.databinding.FragmentHomepageBottomSheetBinding
import id.allana.titipbarangku.databinding.SettingLanguageBottomSheetBinding
import id.allana.titipbarangku.ui.category.CategoryViewModel
import id.allana.titipbarangku.ui.deposit.DepositViewModel
import id.allana.titipbarangku.ui.homepage.HomepageViewModel
import id.allana.titipbarangku.ui.login.LoginActivity
import id.allana.titipbarangku.ui.product.ProductViewModel
import id.allana.titipbarangku.ui.setting.adapter.LanguageAdapter
import id.allana.titipbarangku.ui.setting.saleshistory.MonthlySalesHistoryActivity
import id.allana.titipbarangku.ui.store.StoreViewModel
import id.allana.titipbarangku.util.ConstantValue.SNACKBAR_DURATION
import id.allana.titipbarangku.util.formatDateWithTime
import id.allana.titipbarangku.util.setLanguage
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    private val settingViewModel: SettingViewModel by viewModels()
    private val homepageViewModel: HomepageViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()
    private val depositViewModel: DepositViewModel by viewModels()

    private lateinit var bindingSettingLanguage: SettingLanguageBottomSheetBinding
    private lateinit var bindingUserStoreName: FragmentHomepageBottomSheetBinding
    private val bottomSheetDialog by lazy {
        BottomSheetDialog(this)
    }

    private var userBusinessName = ""

    override fun initView() {
        supportActionBar?.title = getString(R.string.setting)

        getViewBinding().btnEdit.setOnClickListener {
            showUserStoreNameBottomSheet()
        }
        getViewBinding().appLanguage.setOnClickListener {
            showLanguageBottomSheet()
        }
        getViewBinding().btnBackup.setOnClickListener {
            backupDataToFirebase()
        }
        getViewBinding().btnRestore.setOnClickListener {
            restoreDataFromFirebase()
        }
        getViewBinding().deleteAllData.setOnClickListener {
            showAlertDialogDeleteAll()
        }
        getViewBinding().btnLogout.setOnClickListener {
            showAlertDialogLogout()
        }
        getViewBinding().historyTotalSales.setOnClickListener {
            startActivity(Intent(this, MonthlySalesHistoryActivity::class.java))
        }


    }

    private fun getCurrentTime(): String {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val year = currentDateTime.year
        val month = currentDateTime.monthNumber
        val day = currentDateTime.dayOfMonth
        val hour = currentDateTime.hour
        val minute = currentDateTime.minute
        return formatDateWithTime(day, month, year, hour, minute).also { date ->
            homepageViewModel.setLastDateBackup(date)
        }
    }

    private fun showUserStoreNameBottomSheet() {
        bindingUserStoreName = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_homepage_bottom_sheet,
            null,
            false
        )

        bottomSheetDialog.setContentView(bindingUserStoreName.root)

        bindingUserStoreName.etStoreName.setText(userBusinessName)

        bindingUserStoreName.btnSaveStoreName.setOnClickListener {
            val storeName = bindingUserStoreName.etStoreName.text.toString()
            homepageViewModel.setUserStoreName(storeName)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()

    }

    private fun showLanguageBottomSheet() {
        bindingSettingLanguage = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.setting_language_bottom_sheet,
            null,
            false
        )

        bottomSheetDialog.setContentView(bindingSettingLanguage.root)

        val listLanguange = listOf(getString(R.string.indonesian_lang), getString(R.string.english_lang))
        val listView = bindingSettingLanguage.lvLanguage
        listView.adapter = LanguageAdapter(listLanguange, this) { language ->
            changeAppLanguage(language)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun observeData() {
        homepageViewModel.getAppLanguangeLiveData().observe(this) { appLanguage ->
            if (appLanguage == "" || appLanguage == "en") {
                getViewBinding().selectedLanguage.text = getString(R.string.english_lang)
            } else {
                getViewBinding().selectedLanguage.text = getString(R.string.indonesian_lang)
            }
        }
        homepageViewModel.getUserStoreName().observe(this) { userStoreName ->
            userBusinessName = userStoreName
            getViewBinding().userStoreName.text = userBusinessName
        }
        homepageViewModel.getLastDateBackup().observe(this) { date ->
            getViewBinding().dateAndTimeLastBackup.text = date
        }
    }

    private fun changeAppLanguage(language: String) {
        if (language == "Indonesia") {
            homepageViewModel.setAppLanguage("in")
            setLanguage(this, "in")
            recreate()
        } else {
            homepageViewModel.setAppLanguage("en")
            setLanguage(this, "en")
            recreate()
        }
    }

    private fun backupDataToFirebase() {
        backupCategory()
    }

    private fun restoreDataFromFirebase() {
        restoreCategory()
    }

    /**
     * FIRST
     * Backup Category
     */
    private fun backupCategory() {
        categoryViewModel.getAllCategory().observe(this) { categories ->
            settingViewModel.backupCategoryToFirebase(categories) { isCategorySuccess ->
                handleBackupResult(isCategorySuccess, getString(R.string.data_category))
                backupProduct()
            }
        }
    }

    /**
     * SECOND
     * Backup Product
     */
    private fun backupProduct() {
        productViewModel.getAllProduct().observe(this) { products ->
            settingViewModel.backupProductToFirebase(products) { isProductSuccess ->
                handleBackupResult(isProductSuccess, getString(R.string.data_product))
                backupDeposits()
            }
        }
    }

    /**
     * THIRD
     * Backup Deposit
     */
    private fun backupDeposits() {
        depositViewModel.getAllDeposit().observe(this) { deposits ->
            settingViewModel.backupDepositToFirebase(deposits) { isDepositSuccess ->
                handleBackupResult(isDepositSuccess, getString(R.string.data_deposit))
                backupStores()
            }
        }
    }

    /**
     * FOURTH
     * Backup Store
     */
    private fun backupStores() {
        storeViewModel.getAllStore().observe(this) { stores ->
            settingViewModel.backupStoreToFirebase(stores) { isStoreSuccess ->
                handleBackupResult(isStoreSuccess, getString(R.string.data_store))
                backupProductDeposits()
            }
        }
    }

    /**
     * FIFTH
     * Backup Product Deposit
     */
    private fun backupProductDeposits() {
        productViewModel.getAllProductDeposit().observe(this) { productDeposits ->
            settingViewModel.backupProductDepositToFirebase(productDeposits) { isProductDepositSuccess ->
                handleBackupResult(isProductDepositSuccess, getString(R.string.data_product_deposit))
            }
        }
    }

    private fun restoreCategory() {
        settingViewModel.restoreCategoryFromFirebase().observe(this) { listCategoryModelFirebase ->
            val listCategoryModel = listCategoryModelFirebase.map { settingViewModel.convertToCategoryModel(it) }
            Log.d("Homepage Fragment", listCategoryModel.toString())
            settingViewModel.insertAllCategories(listCategoryModel) { isRestoreCategorySuccess ->
                handleRestoreResult(isRestoreCategorySuccess, getString(R.string.data_category))
            }
            restoreProduct()
        }
    }

    private fun restoreProduct() {
        settingViewModel.restoreProductFromFirebase().observe(this) { listProductModelFirebase ->
            val listProductModel = listProductModelFirebase.map { settingViewModel.convertToProductModel(it) }
            Log.d("Homepage Fragment", listProductModel.toString())
            settingViewModel.insertAllProducts(listProductModel) { isRestoreProductSuccess ->
                handleRestoreResult(isRestoreProductSuccess, getString(R.string.data_product))
            }
            restoreStore()
        }
    }

    private fun restoreStore() {
        settingViewModel.restoreStoreFromFirebase().observe(this) { listStoreModelFirebase ->
            val listStoreModel = listStoreModelFirebase.map { settingViewModel.convertToStoreModel(it) }
            Log.d("Homepage Fragment", listStoreModel.toString())
            settingViewModel.insertAllStores(listStoreModel) { isRestoreStoreSuccess ->
                handleRestoreResult(isRestoreStoreSuccess, getString(R.string.data_store))
            }
            restoreDeposit()
        }
    }

    private fun restoreDeposit() {
        settingViewModel.restoreDepositFromFirebase().observe(this) { listDepositModelFirebase ->
            val listDepositModel = listDepositModelFirebase.map { settingViewModel.convertToDepositModel(it) }
            Log.d("Homepage Fragment", listDepositModel.toString())
            settingViewModel.insertAllDeposits(listDepositModel) { isRestoreDepositSuccess ->
                handleRestoreResult(isRestoreDepositSuccess, getString(R.string.data_deposit))
            }
            restoreProductDeposit()
        }
    }

    private fun restoreProductDeposit() {
        settingViewModel.restoreProductDepositFromFirebase().observe(this) { listProductDepositModelFirebase ->
            val listProductDepositModel = listProductDepositModelFirebase.map { settingViewModel.convertToProductDepositModel(it) }
            Log.d("Homepage Fragment", listProductDepositModel.toString())
            settingViewModel.insertAllProductDeposits(listProductDepositModel) { isRestoreProductDepositSuccess ->
                handleRestoreResult(isRestoreProductDepositSuccess, getString(R.string.data_product_deposit))
            }
        }
    }

    private fun handleBackupResult(isSuccess: Boolean, message: String) {
        val successMessage = getString(R.string.success_backup)
        val failureMessage = getString(R.string.failure_backup)
        val resultMessage = if (isSuccess) successMessage else failureMessage
        Snackbar.make(getViewBinding().root, "$message $resultMessage", SNACKBAR_DURATION).show()
        getViewBinding().dateAndTimeLastBackup.text = getCurrentTime()
    }

    private fun handleRestoreResult(isSuccess: Boolean, message: String) {
        val successMessage = getString(R.string.success_restore)
        val failureMessage = getString(R.string.failure_restore)
        val resultMessage = if (isSuccess) successMessage else failureMessage
        Snackbar.make(getViewBinding().root, "$message $resultMessage", SNACKBAR_DURATION).show()
    }

    private fun showAlertDialogDeleteAll() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.delete_all_data))
            setMessage(getString(R.string.msg_delete_all_data))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                handleDeleteAllData()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }


    private fun showAlertDialogLogout() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.logout_account))
            setMessage(getString(R.string.msg_logout_account))
            setPositiveButton(getString(R.string.logout)) { _, _ ->
                handleLogout()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun handleDeleteAllData() {
        /**
         * LIST ALL DATA
         */
        categoryViewModel.getAllCategory().observe(this) {
            settingViewModel.deleteAllCategories(it)
        }
        productViewModel.getAllProduct().observe(this) {
            settingViewModel.deleteAllProducts(it)
        }
        depositViewModel.getAllDeposit().observe(this) {
            settingViewModel.deleteAllDeposits(it)
        }
        storeViewModel.getAllStore().observe(this) {
            settingViewModel.deleteAllStores(it)
        }
        productViewModel.getAllProductDeposit().observe(this) {
            settingViewModel.deleteAllProductDeposits(it)
        }
        Snackbar.make(getViewBinding().root, getString(R.string.success_delete_all_data), SNACKBAR_DURATION).show()
    }

    private fun handleLogout() {
        Log.d("HOMEPAGE FRAGMENT", "BUTTON SIGN OUT: CLICKED!")
        Firebase.auth.signOut()
        homepageViewModel.setAuthUser("", this)
        navigateToLoginScreen()
    }

    private fun navigateToLoginScreen() {
        val intentToMain = Intent(this, LoginActivity::class.java)
        intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentToMain)
    }

}
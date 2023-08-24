package id.allana.titipbarangku.ui.homepage

import android.content.Intent
import android.icu.text.DateFormatSymbols
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentHomepageBinding
import id.allana.titipbarangku.ui.category.CategoryViewModel
import id.allana.titipbarangku.ui.deposit.DepositViewModel
import id.allana.titipbarangku.ui.deposit.adapter.DepositAdapter
import id.allana.titipbarangku.ui.login.LoginActivity
import id.allana.titipbarangku.ui.product.ProductViewModel
import id.allana.titipbarangku.ui.store.StoreViewModel
import id.allana.titipbarangku.util.formatRupiah
import id.allana.titipbarangku.util.snackbar
import java.util.Calendar
import java.util.Locale

class HomepageFragment : BaseFragment<FragmentHomepageBinding>(FragmentHomepageBinding::inflate) {

    private val homepageViewModel: HomepageViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()
    private val depositViewModel: DepositViewModel by viewModels()

    private val depositAdapter: DepositAdapter by lazy {
        DepositAdapter()
    }

    override fun initView() {
        initRecyclerView()
        getCurrentMonth()
        setupMenu()
    }

    override fun observeData() {
        observeUserStoreName()
        observeUnfinishedDeposit()
        observeDatabaseEmpty()
        observeFilteredDeposit()
        observeTotalAmountSales()
    }

    private fun initRecyclerView() {
        getViewBinding().rvUnfinishedDeposit.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = depositAdapter
        }
    }

    private fun getCurrentHour(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> getString(R.string.good_morning)
            in 12..15 -> getString(R.string.good_afternoon)
            in 16..18 -> getString(R.string.good_evening)
            else -> getString(R.string.good_night)
        }
    }

    private fun getCurrentMonth() {
        val monthName = DateFormatSymbols(Locale("id", "ID")).months
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        getViewBinding().tvCurrentMonth.text = monthName[currentMonth]
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.homepage_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_logout -> {
                        showAlertDialog()
                    }
                    R.id.menu_backup -> {
                        backupDataToFirebase()
                    }
                    R.id.menu_restore -> {
                        Log.d("Homepage Fragment", "RESTORE DATA")
                        restoreDataFromFirebase()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun handleLogout() {
        Log.d("HOMEPAGE FRAGMENT", "BUTTON SIGN OUT: CLICKED!")
        Firebase.auth.signOut()
        homepageViewModel.setAuthUser("", requireContext())
        navigateToLoginScreen()
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
        categoryViewModel.getAllCategory().observe(viewLifecycleOwner) { categories ->
            homepageViewModel.backupCategoryToFirebase(categories) { isCategorySuccess ->
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
        productViewModel.getAllProduct().observe(viewLifecycleOwner) { products ->
            homepageViewModel.backupProductToFirebase(products) { isProductSuccess ->
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
        depositViewModel.getAllDeposit().observe(viewLifecycleOwner) { deposits ->
            homepageViewModel.backupDepositToFirebase(deposits) { isDepositSuccess ->
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
        storeViewModel.getAllStore().observe(viewLifecycleOwner) { stores ->
            homepageViewModel.backupStoreToFirebase(stores) { isStoreSuccess ->
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
        productViewModel.getAllProductDeposit().observe(viewLifecycleOwner) { productDeposits ->
            homepageViewModel.backupProductDepositToFirebase(productDeposits) { isProductDepositSuccess ->
                handleBackupResult(isProductDepositSuccess, getString(R.string.data_product_deposit))
            }
        }
    }

    private fun restoreCategory() {
        homepageViewModel.restoreCategoryFromFirebase().observe(viewLifecycleOwner) { listCategoryModelFirebase ->
            val listCategoryModel = listCategoryModelFirebase.map { homepageViewModel.convertToCategoryModel(it) }
            Log.d("Homepage Fragment", listCategoryModel.toString())
            homepageViewModel.insertAllCategories(listCategoryModel) { isRestoreCategorySuccess ->
                handleRestoreResult(isRestoreCategorySuccess, getString(R.string.data_category))
            }
            restoreProduct()
        }
    }

    private fun restoreProduct() {
        homepageViewModel.restoreProductFromFirebase().observe(viewLifecycleOwner) { listProductModelFirebase ->
            val listProductModel = listProductModelFirebase.map { homepageViewModel.convertToProductModel(it) }
            Log.d("Homepage Fragment", listProductModel.toString())
            homepageViewModel.insertAllProducts(listProductModel) { isRestoreProductSuccess ->
                handleRestoreResult(isRestoreProductSuccess, getString(R.string.data_product))
            }
            restoreStore()
        }
    }

    private fun restoreStore() {
        homepageViewModel.restoreStoreFromFirebase().observe(viewLifecycleOwner) { listStoreModelFirebase ->
            val listStoreModel = listStoreModelFirebase.map { homepageViewModel.convertToStoreModel(it) }
            Log.d("Homepage Fragment", listStoreModel.toString())
            homepageViewModel.insertAllStores(listStoreModel) { isRestoreStoreSuccess ->
                handleRestoreResult(isRestoreStoreSuccess, getString(R.string.data_store))
            }
            restoreDeposit()
        }
    }

    private fun restoreDeposit() {
        homepageViewModel.restoreDepositFromFirebase().observe(viewLifecycleOwner) { listDepositModelFirebase ->
            val listDepositModel = listDepositModelFirebase.map { homepageViewModel.convertToDepositModel(it) }
            Log.d("Homepage Fragment", listDepositModel.toString())
            homepageViewModel.insertAllDeposits(listDepositModel) { isRestoreDepositSuccess ->
                handleRestoreResult(isRestoreDepositSuccess, getString(R.string.data_deposit))
            }
            restoreProductDeposit()
        }
    }

    private fun restoreProductDeposit() {
        homepageViewModel.restoreProductDepositFromFirebase().observe(viewLifecycleOwner) { listProductDepositModelFirebase ->
            val listProductDepositModel = listProductDepositModelFirebase.map { homepageViewModel.convertToProductDepositModel(it) }
            Log.d("Homepage Fragment", listProductDepositModel.toString())
            homepageViewModel.insertAllProductDeposits(listProductDepositModel) { isRestoreProductDepositSuccess ->
                handleRestoreResult(isRestoreProductDepositSuccess, getString(R.string.data_product_deposit))
            }
        }
    }

    private fun handleBackupResult(isSuccess: Boolean, message: String) {
        val successMessage = getString(R.string.success_backup)
        val failureMessage = getString(R.string.failure_backup)
        val resultMessage = if (isSuccess) successMessage else failureMessage
        requireView().snackbar("$message $resultMessage", R.id.nav_view)
    }

    private fun handleRestoreResult(isSuccess: Boolean, message: String) {
        val successMessage = getString(R.string.success_restore)
        val failureMessage = getString(R.string.failure_restore)
        val resultMessage = if (isSuccess) successMessage else failureMessage
        requireView().snackbar("$message $resultMessage", R.id.nav_view)
    }

    private fun observeUserStoreName() {
        homepageViewModel.getUserStoreName().observe(viewLifecycleOwner) { userStoreName ->
            if (userStoreName == "") {
                handleShowHomepageBottomSheet()
            } else {
                getViewBinding().tvGreetingBusinessName.text = getString(
                    R.string.greeting_business_name,
                    getCurrentHour(),
                    userStoreName
                )
            }
        }
    }

    private fun handleShowHomepageBottomSheet() {
        val actionToHomepageBottomSheet = HomepageFragmentDirections.actionNavigationHomepageToHomepageBottomSheetFragment()
        findNavController().navigate(actionToHomepageBottomSheet)
    }

    private fun observeUnfinishedDeposit() {
        homepageViewModel.getAllUnfinishedDeposit().observe(viewLifecycleOwner) { listDepositWithStore ->
            homepageViewModel.checkDatabaseEmpty(listDepositWithStore)
            depositAdapter.submitList(listDepositWithStore)
        }
    }

    private fun observeDatabaseEmpty() {
        homepageViewModel.checkDatabaseEmptyLiveData().observe(viewLifecycleOwner) { isEmpty ->
            stateDataEmpty(isEmpty)
        }
    }

    private fun observeFilteredDeposit() {
        homepageViewModel.getAllDeposit().observe(viewLifecycleOwner) { listDeposit ->
            homepageViewModel.getAllProductInDeposit().observe(viewLifecycleOwner) { listProductInDeposit ->
                homepageViewModel.getAllFilteredDeposit(listDeposit, listProductInDeposit)
                observeTotalAmountSales()
            }
        }
    }

    private fun observeTotalAmountSales() {
        homepageViewModel.getAllFilterdDepositLiveData().observe(viewLifecycleOwner) { listProductInDepositFiltered ->
                homepageViewModel.calculateTotalAmount(listProductInDepositFiltered)
                homepageViewModel.calculateTotalAmountLiveData().observe(viewLifecycleOwner) { totalAmount ->
                    getViewBinding().tvTotalAmountSales.text = formatRupiah(totalAmount.toString()) }
            }
    }

    private fun stateDataEmpty(isEmpty: Boolean) {
        getViewBinding().apply {
            ivStateDataEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            tvStateDataEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToLoginScreen() {
        val intentToMain = Intent(requireContext(), LoginActivity::class.java)
        intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentToMain)
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
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


}

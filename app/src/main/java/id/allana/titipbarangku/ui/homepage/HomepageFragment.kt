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
            in 0..11 -> "Selamat pagi!"
            in 12..15 -> "Selamat siang!"
            in 16..18 -> "Selamat sore!"
            else -> "Selamat malam!"
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
                menuInflater.inflate(R.menu.homepage_deposit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_logout -> {
                        handleLogout()
                    }
                    R.id.menu_backup -> {
                        backupDataToFirebase()
                    }
                    R.id.menu_restore -> {
                        // Handle menu_restore
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun handleLogout() {
        Log.d("HOMEPAGE FRAGMENT", "BUTTON SIGN OUT: CLICKED!")
        Firebase.auth.signOut()
        navigateToLoginScreen()
    }

    private fun backupDataToFirebase() {
        backupCategory()
    }

    /**
     * FIRST
     * Backup Category
     */
    private fun backupCategory() {
        categoryViewModel.getAllCategory().observe(viewLifecycleOwner) { categories ->
            homepageViewModel.backupCategoryToFirebase(categories) { isCategorySuccess ->
                handleBackupResult(isCategorySuccess, "Data kategori")
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
                handleBackupResult(isProductSuccess, "Data produk")
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
                handleBackupResult(isDepositSuccess, "Data deposit")
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
                handleBackupResult(isStoreSuccess, "Data toko")
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
                handleBackupResult(isProductDepositSuccess, "Data produk deposit")
            }
        }
    }

    private fun handleBackupResult(isSuccess: Boolean, message: String) {
        val successMessage = "berhasil dicadangkan"
        val failureMessage = "gagal dicadangkan"
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


}

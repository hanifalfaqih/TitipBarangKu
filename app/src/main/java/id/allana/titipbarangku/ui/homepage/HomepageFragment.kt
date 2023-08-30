package id.allana.titipbarangku.ui.homepage

import android.icu.text.DateFormatSymbols
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
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentHomepageBinding
import id.allana.titipbarangku.ui.deposit.adapter.DepositAdapter
import id.allana.titipbarangku.util.formatRupiah
import java.util.Calendar
import java.util.Locale

class HomepageFragment : BaseFragment<FragmentHomepageBinding>(FragmentHomepageBinding::inflate) {

    private val homepageViewModel: HomepageViewModel by viewModels()

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
                    android.R.id.home -> {
                        findNavController().navigateUp()
                    }
                    R.id.menu_setting -> {
                        actionToSetting()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun actionToSetting() {
        findNavController().navigate(R.id.action_navigation_homepage_to_settingActivity)
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

}

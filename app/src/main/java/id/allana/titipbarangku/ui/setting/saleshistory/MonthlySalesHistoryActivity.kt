package id.allana.titipbarangku.ui.setting.saleshistory

import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.titipbarangku.data.base.BaseActivity
import id.allana.titipbarangku.databinding.ActivityMonthlySalesHistoryBinding
import id.allana.titipbarangku.ui.homepage.HomepageViewModel
import id.allana.titipbarangku.ui.setting.saleshistory.adapter.MonthlySalesHistoryAdapter

class MonthlySalesHistoryActivity : BaseActivity<ActivityMonthlySalesHistoryBinding>(ActivityMonthlySalesHistoryBinding::inflate)
{

    private val homepageViewModel: HomepageViewModel by viewModels()

    private val monthlySalesHistoryAdapter: MonthlySalesHistoryAdapter by lazy {
        MonthlySalesHistoryAdapter()
    }

    override fun initView() {}

    override fun observeData() {
        observeFilteredDeposit()
    }

    private fun observeFilteredDeposit() {
        homepageViewModel.getAllDeposit().observe(this) { listDeposit ->
            homepageViewModel.getAllProductInDeposit().observe(this) { listProductInDeposit ->
                homepageViewModel.getAllFilteredDepositEachMonth(listDeposit, listProductInDeposit)
                observeTotalAmountSalesEachMonth()
            }
        }
    }

    private fun observeTotalAmountSalesEachMonth() {
        homepageViewModel.getAllFilteredDepositEachMonthLiveData().observe(this) { listProductInDepositFiltered ->
            homepageViewModel.calculateTotalAmountEachMonth(listProductInDepositFiltered)
            homepageViewModel.calculateTotalAmountEachMonthLiveData().observe(this) { monthWithTotalAmount ->
                setDataAdapter(monthWithTotalAmount)
            }
        }
    }

    private fun setDataAdapter(data: Map<Int, Int>) {
        monthlySalesHistoryAdapter.data = data
        Log.d(MonthlySalesHistoryActivity::class.java.simpleName, data.toString())
        initRecyclerView()
    }

    private fun initRecyclerView() {
        getViewBinding().rvMonthlySalesHistory.apply {
            layoutManager = LinearLayoutManager(this@MonthlySalesHistoryActivity)
            adapter = monthlySalesHistoryAdapter
        }
    }
}
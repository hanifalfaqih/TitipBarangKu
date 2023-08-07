package id.allana.titipbarangku.ui.homepage

import android.icu.text.DateFormatSymbols
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentHomepageBinding
import id.allana.titipbarangku.ui.deposit.adapter.DepositAdapter
import id.allana.titipbarangku.util.formatRupiah
import java.util.Calendar
import java.util.Locale

class HomepageFragment : BaseFragment<FragmentHomepageBinding>(FragmentHomepageBinding::inflate) {

    private val viewModel: HomepageViewModel by viewModels()

    private val depositAdapter: DepositAdapter by lazy {
        DepositAdapter()
    }

    override fun initView() {
        initRecyclerView()
        getCurrentMonth()
        viewModel.getAllDeposit()
        viewModel.calculateTotalAmountLiveData()
    }

    override fun observeData() {
        viewModel.getAllUnfinishedDeposit().observe(viewLifecycleOwner) { listDepositWithStore ->
            viewModel.checkDatabaseEmpty(listDepositWithStore)
            depositAdapter.submitList(listDepositWithStore)
        }

        viewModel.checkDatabaseEmptyLiveData().observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                stateDataEmpty(true)
            } else {
                stateDataEmpty(false)
            }
        }

        /**
         * first, get data LIST DEPOSIT
         */
        viewModel.getAllDeposit().observe(viewLifecycleOwner) { listDeposit ->

            /**
             * second, get data LIST PRODUCT IN DEPOSIT
             * LIST DEPOSIT & LIST PRODUCT IN DEPOSIT have relation from idDeposit
             */
            viewModel.getAllProductInDeposit().observe(viewLifecycleOwner) { listProductInDeposit ->
                /**
                 * third, filter data LIST DEPOSIT & LIST PRODUCT IN DEPOSIT based on current month
                 * return as LIST PRODUCT IN DEPOSIT
                 */
                viewModel.getAllFilteredDeposit(listDeposit, listProductInDeposit)
            }
        }

        /**
         * fourth, calculate LIST PRODUCT IN DEPOSIT to calculate TOTAL AMOUNT SALES current month
         */
        viewModel.getAllFilterdDepositLiveData().observe(viewLifecycleOwner) { listProductInDepositFiltered ->
            viewModel.calculateTotalAmountLiveData(listProductInDepositFiltered)
        }

        /**
         * fifth, return calculate TOTAL AMOUNT SALES to INTEGER
         * from INTEGER convert with format local currency
         */
        viewModel.calculateTotalAmountLiveData().observe(viewLifecycleOwner) { totalAmount ->
            getViewBinding().tvTotalAmountSales.text = formatRupiah(totalAmount.toString())
        }
    }

    private fun initRecyclerView() {
        getViewBinding().rvUnfinishedDeposit.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomepageFragment.depositAdapter
        }
    }

    private fun getCurrentMonth() {
        val monthName = DateFormatSymbols(Locale("id", "ID")).months
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        getViewBinding().tvCurrentMonth.text = monthName[currentMonth]
    }

    private fun stateDataEmpty(isEmpty: Boolean) {
        getViewBinding().also {
            if (isEmpty) {
                it.ivStateDataEmpty.visibility = View.VISIBLE
                it.tvStateDataEmpty.visibility = View.VISIBLE
            } else {
                it.ivStateDataEmpty.visibility = View.GONE
                it.tvStateDataEmpty.visibility = View.GONE
            }
        }
    }

}
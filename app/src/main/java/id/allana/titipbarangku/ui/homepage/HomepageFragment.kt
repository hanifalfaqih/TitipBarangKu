package id.allana.titipbarangku.ui.homepage

import android.icu.text.DateFormatSymbols
import androidx.fragment.app.viewModels
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentHomepageBinding
import id.allana.titipbarangku.util.formatRupiah
import java.util.Calendar
import java.util.Locale

class HomepageFragment : BaseFragment<FragmentHomepageBinding>(FragmentHomepageBinding::inflate) {

    private val viewModel: HomepageViewModel by viewModels()

    override fun initView() {
        getCurrentMonth()
        viewModel.getAllDeposit()
    }

    override fun observeData() {
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

//                    Log.d(HomepageFragment::class.java.simpleName, formatRupiah(totalAmount.toString()))
                }
            }
        }
    }

    private fun getCurrentMonth() {
        val monthName = DateFormatSymbols(Locale("id", "ID")).months
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        getViewBinding().tvCurrentMonth.text = monthName[currentMonth]
    }

}
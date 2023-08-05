package id.allana.titipbarangku.ui.homepage

import android.util.Log
import androidx.fragment.app.viewModels
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentHomepageBinding
import id.allana.titipbarangku.util.formatRupiah

class HomepageFragment : BaseFragment<FragmentHomepageBinding>(FragmentHomepageBinding::inflate) {

    private val viewModel: HomepageViewModel by viewModels()

    override fun initView() {
        viewModel.getAllDeposit()
    }

    override fun observeData() {
        viewModel.getAllDeposit().observe(viewLifecycleOwner) { listDeposit ->
            viewModel.getAllProductInDeposit().observe(viewLifecycleOwner) { listProductInDeposit ->
                viewModel.getAllFilteredDeposit(listDeposit, listProductInDeposit)
            }
            viewModel.getAllFilterdDepositLiveData().observe(viewLifecycleOwner) { listProductInDepositFiltered ->
                viewModel.calculateTotalAmountLiveData(listProductInDepositFiltered)
            }
            viewModel.calculateTotalAmountLiveData().observe(viewLifecycleOwner) { totalAmount ->
//            getViewBinding().tvTotalAmountSales.text = formatRupiah(totalAmount.toString())
                Log.d(HomepageFragment::class.java.simpleName, formatRupiah(totalAmount.toString()))
            }
        }
    }

}
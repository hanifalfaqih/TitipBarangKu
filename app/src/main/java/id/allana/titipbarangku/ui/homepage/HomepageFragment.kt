package id.allana.titipbarangku.ui.homepage

import android.util.Log
import androidx.fragment.app.viewModels
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentHomepageBinding

class HomepageFragment : BaseFragment<FragmentHomepageBinding>(FragmentHomepageBinding::inflate) {

    private val viewModel: HomepageViewModel by viewModels()

    override fun initView() {
        viewModel.getAllDeposit()
        viewModel.getAllProductInDeposit()
    }

    override fun observeData() {
        viewModel.getAllDeposit().observe(viewLifecycleOwner) { listDeposit ->
            viewModel.getAllProductInDeposit().observe(viewLifecycleOwner) { listProductInDeposit ->
                viewModel.getAllFilteredDeposit(listDeposit, listProductInDeposit)
            }
        }
        viewModel.getAllFilterdDepositLiveData().observe(viewLifecycleOwner) { listProductInDepositFiltered ->
            viewModel.calculateTotalAmountLiveData(listProductInDepositFiltered)
        }
        viewModel.calculateTotalAmountLiveData().observe(viewLifecycleOwner) { totalAmount ->
            Log.d("HOMEPAGE FRAGMENT", totalAmount.toString())
        }
    }

}
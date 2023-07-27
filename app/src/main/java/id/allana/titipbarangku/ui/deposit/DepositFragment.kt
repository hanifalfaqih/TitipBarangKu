package id.allana.titipbarangku.ui.deposit

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentDepositBinding

class DepositFragment : BaseFragment<FragmentDepositBinding>(FragmentDepositBinding::inflate) {

    private val viewModel: DepositViewModel by viewModels()

    override fun initView() {
        getViewBinding().fabAddDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_deposit_to_addDepositFragment)
        }
    }

    override fun observeData() {

    }

}
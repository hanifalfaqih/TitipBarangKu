package id.allana.titipbarangku.ui.deposit

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentDepositBinding

class DepositFragment : BaseFragment<FragmentDepositBinding>(FragmentDepositBinding::inflate) {

    private val viewModel: DepositViewModel by viewModels()
    private val depositAdapter by lazy {
        DepositAdapter()
    }

    override fun initView() {
        initRecyclerView()

        getViewBinding().fabAddDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_deposit_to_addDepositHolderActivity)
        }
    }

    private fun initRecyclerView() {
        getViewBinding().rvDeposit.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DepositFragment.depositAdapter
        }
    }

    override fun observeData() {
        viewModel.getAllDeposit().observe(viewLifecycleOwner) { list ->
            viewModel.checkDeposit(list)
            depositAdapter.submitList(list)
        }
        viewModel.checkDepositLiveData().observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                stateDataEmpty(true)
            } else {
                stateDataEmpty(false)
            }
        }
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
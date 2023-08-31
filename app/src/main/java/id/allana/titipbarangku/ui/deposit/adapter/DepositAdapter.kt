package id.allana.titipbarangku.ui.deposit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.model.DepositWithStore
import id.allana.titipbarangku.data.model.Status
import id.allana.titipbarangku.databinding.ItemDepositBinding
import id.allana.titipbarangku.ui.deposit.DepositFragmentDirections
import id.allana.titipbarangku.ui.homepage.HomepageFragmentDirections

class DepositAdapter: ListAdapter<DepositWithStore, DepositAdapter.DepositViewHolder>(
    DepositComparator()
) {
    inner class DepositViewHolder(private val binding: ItemDepositBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DepositWithStore) {
            binding.also {
                it.tvStoreName.text = data.store?.name
                it.tvDateDeposit.text = itemView.context.getString(R.string.format_date_deposit, data.deposit.startDateDeposit, data.deposit.finishDateDeposit)
                when (data.deposit.status) {
                    Status.DEPOSIT -> {
                        it.tvStatusDeposit.text = itemView.context.getString(R.string.deposit_caps)
                        it.tvStatusDeposit.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_500))
                    }
                    else -> {
                        it.tvStatusDeposit.text = itemView.context.getString(R.string.finish_caps)
                        it.tvStatusDeposit.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue_200))
                    }
                }
            }
            itemView.setOnClickListener {
                val navController = it.findNavController()
                when (navController.currentDestination?.id) {
                    R.id.navigation_homepage -> {
                        val actionToDetailDepositFromHomepageFragment = HomepageFragmentDirections.actionNavigationHomepageToDetailDepositFragment(data)
                        it.findNavController().navigate(actionToDetailDepositFromHomepageFragment)
                    }
                    R.id.navigation_deposit -> {
                        val actionToDetailDepositFromDepositFragment = DepositFragmentDirections.actionNavigationDepositToDetailDepositFragment(data)
                        it.findNavController().navigate(actionToDetailDepositFromDepositFragment)
                    }
                }
            }
        }
    }

    class DepositComparator: DiffUtil.ItemCallback<DepositWithStore>() {
        override fun areItemsTheSame(
            oldItem: DepositWithStore,
            newItem: DepositWithStore
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: DepositWithStore,
            newItem: DepositWithStore
        ): Boolean {
            return oldItem.deposit.id == newItem.deposit.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DepositViewHolder {
        val binding = ItemDepositBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DepositViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DepositViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
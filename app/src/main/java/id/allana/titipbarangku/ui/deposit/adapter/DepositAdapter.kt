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

class DepositAdapter: ListAdapter<DepositWithStore, DepositAdapter.DepositViewHolder>(
    DepositComparator()
) {
    inner class DepositViewHolder(private val binding: ItemDepositBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DepositWithStore) {
            binding.also {
                it.tvStoreName.text = data.store?.name
                it.tvStartDateDeposit.text = data.deposit.startDateDeposit
                it.tvEndDateDeposit.text = data.deposit.finishDateDeposit
                when (data.deposit.status) {
                    Status.DEPOSIT -> {
                        it.tvStatusDeposit.text = "DEPOSIT"
                        it.tvStatusDeposit.setTextColor(ContextCompat.getColor(itemView.context, R.color.purple_500))
                    }
                    else -> {
                        it.tvStatusDeposit.text = "FINISH"
                        it.tvStatusDeposit.setTextColor(ContextCompat.getColor(itemView.context, R.color.teal_200))
                    }
                }
            }
            itemView.setOnClickListener {
                val actionToDetailDeposit = DepositFragmentDirections.actionNavigationDepositToDetailDepositFragment(data)
                it.findNavController().navigate(actionToDetailDeposit)
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
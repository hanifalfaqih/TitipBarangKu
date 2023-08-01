package id.allana.titipbarangku.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.databinding.ItemDepositInDetailStoreBinding

class DetailStoreAdapter: ListAdapter<DepositModel, DetailStoreAdapter.DetailStoreViewHolder>(DetailStoreComparator()) {

    inner class DetailStoreViewHolder(private val binding: ItemDepositInDetailStoreBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DepositModel) {
            binding.also {
                it.tvStartDateDeposit.text = data.startDateDeposit
                it.tvEndDateDeposit.text = data.finishDateDeposit
                it.tvStatusDeposit.text = data.status.name
            }
        }
    }

    class DetailStoreComparator: DiffUtil.ItemCallback<DepositModel>() {
        override fun areItemsTheSame(oldItem: DepositModel, newItem: DepositModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DepositModel, newItem: DepositModel): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailStoreAdapter.DetailStoreViewHolder {
        val binding = ItemDepositInDetailStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailStoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailStoreAdapter.DetailStoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
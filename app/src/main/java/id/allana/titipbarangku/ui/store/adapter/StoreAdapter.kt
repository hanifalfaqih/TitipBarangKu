package id.allana.titipbarangku.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.databinding.ItemStoreBinding
import id.allana.titipbarangku.ui.store.StoreFragmentDirections

class StoreAdapter(private var itemStore: (StoreModel) -> Unit): ListAdapter<StoreModel, StoreAdapter.StoreViewHolder>(StoreComparator()) {

    inner class StoreViewHolder(private val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoreModel) {
            binding.also {
                it.tvStoreName.text = data.name
                it.tvStoreAddress.text = data.address
                it.tvStoreOwnerName.text = data.ownerName
                it.tvStoreOwnerPhoneNumber.text = data.ownerPhoneNumber

                it.btnEdit.setOnClickListener { view ->
                    val actionToStoreBottomSheet = StoreFragmentDirections.actionNavigationStoreToStoreBottomSheetFragment(data)
                    view.findNavController().navigate(actionToStoreBottomSheet)
                }
                it.btnDelete.setOnClickListener {
                    itemStore(data)
                }
            }
        }
    }

    class StoreComparator: DiffUtil.ItemCallback<StoreModel>() {
        override fun areItemsTheSame(oldItem: StoreModel, newItem: StoreModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StoreModel, newItem: StoreModel): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
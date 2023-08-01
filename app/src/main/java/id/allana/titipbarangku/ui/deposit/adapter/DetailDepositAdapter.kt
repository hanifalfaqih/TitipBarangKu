package id.allana.titipbarangku.ui.deposit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.databinding.ItemProductInDetailDepositBinding

class DetailDepositAdapter: ListAdapter<ProductDepositWithProduct, DetailDepositAdapter.DetailDepositViewHolder>(DetailDepositComparator()) {

    inner class DetailDepositViewHolder(private val binding: ItemProductInDetailDepositBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductDepositWithProduct) {
            binding.also {
                it.tvProductName.text = data.product?.name
                it.tvProductQuantity.text = data.productDeposit.quantity.toString()
            }
        }
    }

    class DetailDepositComparator: DiffUtil.ItemCallback<ProductDepositWithProduct>() {
        override fun areItemsTheSame(
            oldItem: ProductDepositWithProduct,
            newItem: ProductDepositWithProduct
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductDepositWithProduct,
            newItem: ProductDepositWithProduct
        ): Boolean {
            return oldItem.productDeposit.id == newItem.productDeposit.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailDepositAdapter.DetailDepositViewHolder {
        val binding = ItemProductInDetailDepositBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailDepositViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetailDepositAdapter.DetailDepositViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}
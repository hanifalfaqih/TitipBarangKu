package id.allana.titipbarangku.ui.deposit.add_product_deposit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.databinding.ItemProductInDepositBinding
import id.allana.titipbarangku.ui.deposit.add_product_deposit.AddProductDepositFragmentDirections

class AddProductDepositAdapter(private var itemProductDeposit: (ProductDepositWithProduct) -> Unit): ListAdapter<ProductDepositWithProduct, AddProductDepositAdapter.AddProductDepositViewHolder>(ProductDepositComparator()) {

    inner class AddProductDepositViewHolder(private val binding: ItemProductInDepositBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductDepositWithProduct) {
            binding.also {
                it.tvProductName.text = data.product?.name
                it.tvProductQuantity.text = itemView.context.getString(R.string.format_show_list, data.productDeposit.quantity.toString())

                it.btnEdit.setOnClickListener { view ->
                    val actionToProductBottomSheet = AddProductDepositFragmentDirections.actionProductDepositFragmentToProductDepositBottomSheetFragment(data.productDeposit)
                    view.findNavController().navigate(actionToProductBottomSheet)
                }

                it.btnDelete.setOnClickListener {
                    itemProductDeposit(data)
                }
            }
        }
    }

    class ProductDepositComparator: DiffUtil.ItemCallback<ProductDepositWithProduct>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductDepositViewHolder {
        val binding = ItemProductInDepositBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddProductDepositViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddProductDepositViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

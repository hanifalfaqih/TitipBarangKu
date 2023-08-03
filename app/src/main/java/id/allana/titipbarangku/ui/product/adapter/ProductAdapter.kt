package id.allana.titipbarangku.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.databinding.ItemProductBinding
import id.allana.titipbarangku.ui.product.ProductFragmentDirections
import id.allana.titipbarangku.util.formatRupiah

class ProductAdapter(private var itemProduct: (ProductModel) -> Unit): ListAdapter<ProductWithCategory, ProductAdapter.ProductViewHolder>(ProductComparator()) {

    inner class ProductViewHolder(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductWithCategory) {
            binding.also {
                it.tvProductName.text = data.product.name
                it.tvProductPrice.text = itemView.context.getString(R.string.format_show_list, formatRupiah(data.product.price))
                it.tvProductCategory.text = itemView.context.getString(R.string.format_show_list, data.category?.categoryName)

                it.btnEdit.setOnClickListener { view ->
                    val actionToProductBottomSheet = ProductFragmentDirections.actionNavigationProductToProductBottomSheetFragment(data.product)
                    view.findNavController().navigate(actionToProductBottomSheet)
                }
                it.btnDelete.setOnClickListener {
                    itemProduct(data.product)
                }
            }
        }
    }

    class ProductComparator: DiffUtil.ItemCallback<ProductWithCategory>() {
        override fun areItemsTheSame(
            oldItem: ProductWithCategory,
            newItem: ProductWithCategory
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductWithCategory,
            newItem: ProductWithCategory
        ): Boolean {
            return oldItem.product.id == newItem.product.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
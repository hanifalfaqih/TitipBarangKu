package id.allana.titipbarangku.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.databinding.ItemProductBinding

class ProductAdapter: ListAdapter<ProductWithCategory, ProductAdapter.ProductViewHolder>(ProductComparator()) {

    inner class ProductViewHolder(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductWithCategory) {
            binding.productWithCategory = data
            binding.executePendingBindings()
//            binding.also {
//                it.tvProductCategory.text = data.category?.categoryName
//                it.tvProductName.text = data.product.name
//                it.tvProductPrice.text = data.product.price
//            }
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
            return oldItem.category!!.id == newItem.category!!.id
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
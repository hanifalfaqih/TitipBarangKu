package id.allana.titipbarangku.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.databinding.ItemCategoryBinding
import id.allana.titipbarangku.ui.category.CategoryBottomSheetFragment
import id.allana.titipbarangku.ui.category.CategoryBottomSheetFragmentDirections
import id.allana.titipbarangku.ui.category.CategoryFragment
import id.allana.titipbarangku.ui.category.CategoryFragmentDirections

class CategoryAdapter(private val itemCategory: (CategoryModel) -> Unit): ListAdapter<CategoryModel, CategoryAdapter.CategoryViewHolder>(CategoryComparator()) {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoryModel) {
            binding.also {
                it.tvCategoryName.text = data.categoryName

                it.btnEdit.setOnClickListener { view ->
                    val actionToCategoryBottomSheet = CategoryFragmentDirections.actionNavigationCategoryToCategoryBottomSheetFragment(data.categoryName)
                    view.findNavController().navigate(actionToCategoryBottomSheet)
                }
                it.btnDelete.setOnClickListener {
                    itemCategory(data)
                }

            }
        }
    }

    class CategoryComparator: DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
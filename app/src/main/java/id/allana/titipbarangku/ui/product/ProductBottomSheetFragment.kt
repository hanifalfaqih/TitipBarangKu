package id.allana.titipbarangku.ui.product

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.databinding.FragmentProductBottomSheetBinding
import id.allana.titipbarangku.ui.category.CategoryViewModel

class ProductBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentProductBottomSheetBinding>(
    FragmentProductBottomSheetBinding::inflate
) {

    private val viewModel: ProductViewModel by viewModels()
    private val categoriViewModel: CategoryViewModel by viewModels()

    private var categoryName = arrayListOf<String>()
    private var listCategory = listOf<CategoryModel>()
    private var categoryId = 0

    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerCategory: AutoCompleteTextView

    private val args by navArgs<ProductBottomSheetFragmentArgs>()

    override fun initView() {
        spinnerCategory = getViewBinding().textDropdownCategory
        spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryName
        )
        spinnerCategory.setAdapter(spinnerAdapter)

        categoriViewModel.getAllCategory().observe(viewLifecycleOwner) { list ->
            listCategory = list
            for (data in listCategory) {
                categoryName.add(data.categoryName)
            }
            spinnerAdapter.notifyDataSetChanged()
        }

        getViewBinding().btnAddProduct.setOnClickListener {
            insertProduct(0)
        }
        spinnerCategory.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedCategory = listCategory[position]
                categoryId = selectedCategory.id
            }

        args.productData?.let { productModel ->
            categoriViewModel.getAllCategory().observe(viewLifecycleOwner) { list ->
                for (data in list) {
                    if (data.id == productModel.idCategory) {
                        spinnerCategory.setText(data.categoryName)
                    }
                }
            }
            setDataToView(productModel)
            getViewBinding().btnAddProduct.text = getString(R.string.update)
            getViewBinding().btnAddProduct.setOnClickListener {
                insertProduct(productModel.id)
            }
        }
    }

    private fun setDataToView(data: ProductModel) {
        getViewBinding().apply {
            etProductName.setText(data.name)
            etProductPrice.setText(data.price)
        }
    }

    private fun insertProduct(id: Int) {
        if (validateForm()) {
            val product = ProductModel(
                id,
                name = getViewBinding().etProductName.text.toString(),
                price = getViewBinding().etProductPrice.text.toString(),
                idCategory = categoryId
            )

            val successMessage = if (id == 0) {
                viewModel.insertProduct(product)
                getString(R.string.success_add_product)
            } else {
                viewModel.updateProduct(product)
                getString(R.string.success_update_product)
            }

            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                successMessage,
                Snackbar.LENGTH_SHORT
            ).show().also { this.dismiss() }
        }
    }

    private fun validateForm(): Boolean {
        val textProductName = getViewBinding().etProductName.text.toString().trim()
        val textProductPrice = getViewBinding().etProductPrice.text.toString().trim()

        val isFormValid: Boolean

        when {
            textProductName.isEmpty() -> {
                isFormValid = false
                getViewBinding().etProductName.error = "Nama produk harus diisi!"
            }

            textProductPrice.isEmpty() -> {
                isFormValid = false
                getViewBinding().etProductPrice.error = "Harga produk harus diisi!"
            }

            else -> {
                isFormValid = true
            }
        }
        return isFormValid
    }
}
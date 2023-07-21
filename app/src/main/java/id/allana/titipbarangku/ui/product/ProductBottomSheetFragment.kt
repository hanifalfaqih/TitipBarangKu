package id.allana.titipbarangku.ui.product

import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
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

    override fun initView() {
        val spinnerCategory = getViewBinding().textDropdownCategory
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
            insertProduct()
        }
        getViewBinding().textDropdownCategory.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedCategory = listCategory[position]
                categoryId = selectedCategory.id
            }
    }

    private fun insertProduct() {
        if (validateForm()) {
            val product = ProductModel(
                0,
                name = getViewBinding().etProductName.text.toString(),
                price = getViewBinding().etProductPrice.text.toString(),
                idCategory = categoryId
            )
            viewModel.insertProduct(product)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Berhasil tambah toko",
                Snackbar.LENGTH_SHORT
            ).show().also {
                this@ProductBottomSheetFragment.dismiss()
            }
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
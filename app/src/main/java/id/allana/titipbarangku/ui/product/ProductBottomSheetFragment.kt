package id.allana.titipbarangku.ui.product

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.databinding.FragmentProductBottomSheetBinding
import id.allana.titipbarangku.ui.category.CategoryViewModel
import id.allana.titipbarangku.util.convertToInt
import id.allana.titipbarangku.util.formatRupiah
import id.allana.titipbarangku.util.snackbar

class ProductBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentProductBottomSheetBinding>(
    FragmentProductBottomSheetBinding::inflate
) {

    private val viewModel: ProductViewModel by viewModels()
    private val categoriViewModel: CategoryViewModel by viewModels()

    private var categoryName = arrayListOf<String>()
    private var listCategory = listOf<CategoryModel>()
    private var idCategory = 0

    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerCategory: AutoCompleteTextView

    private val args by navArgs<ProductBottomSheetFragmentArgs>()

    override fun initView() {
        getViewBinding().etProductPrice.addTextChangedListener(currencyTextWatcher)
        /**
         * init spinner category from textDropdownCategory
         */
        spinnerCategory = getViewBinding().textDropdownCategory

        /**
         * init spinnerAdapter and set to spinnerCategory
         */
        spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryName
        )
        spinnerCategory.setAdapter(spinnerAdapter)

        /**
         * get all category to set data for spinnerAdapter
         */
        categoriViewModel.getAllCategory().observe(viewLifecycleOwner) { list ->
            listCategory = list

            for (data in listCategory) {
                categoryName.add(data.categoryName)
            }
            spinnerAdapter.notifyDataSetChanged()

            /**
             * update product
             */
            args.productData?.let { productModel ->
                val selectedCategory = listCategory.find { it.id == productModel.idCategory }
                selectedCategory?.let {
                    val getCategoryName = it.categoryName
                    spinnerCategory.setText(getCategoryName, false)
                    idCategory = it.id
                }

                setDataToView(productModel)
                getViewBinding().btnAddProduct.text = getString(R.string.update)
                getViewBinding().btnAddProduct.setOnClickListener {
                    insertProduct(productModel.id, idCategory)
                }
            }
        }

        /**
         * get id category from itemClick in spinner
         */
        spinnerCategory.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedCategory = listCategory[position]
                idCategory = selectedCategory.id
            }

        /**
         * add product
         */
        getViewBinding().btnAddProduct.setOnClickListener {
            insertProduct(0, idCategory)
        }
    }

    private fun setDataToView(data: ProductModel) {
        getViewBinding().apply {
            etProductName.setText(data.name)
            etProductPrice.setText(formatRupiah(data.price.toString()))
        }
    }

    private fun insertProduct(id: Int, idCategory: Int) {
        if (validateForm()) {
            val product = ProductModel(
                id,
                name = getViewBinding().etProductName.text.toString(),
                price = convertToInt(getViewBinding().etProductPrice.text.toString()) ,
                idCategory = idCategory
            )

            /**
             * if id == 0 it will add product, and if id != 0 it will update
             */
            val successMessage = if (id == 0) {
                viewModel.insertProduct(product)
                getString(R.string.success_add_product)
            } else {
                viewModel.updateProduct(product)
                getString(R.string.success_update_product)
            }

            requireActivity().findViewById<View>(android.R.id.content).snackbar(successMessage, R.id.fab_add_product).also { this.dismiss() }
        }
    }

    private fun validateForm(): Boolean {
        val textProductName = getViewBinding().etProductName.text.toString().trim()
        val textProductPrice = getViewBinding().etProductPrice.text.toString().trim()

        val isFormValid: Boolean

        when {
            textProductName.isEmpty() -> {
                isFormValid = false
                getViewBinding().etProductName.error = getString(R.string.product_name_must_be_filled)
            }

            textProductPrice.isEmpty() -> {
                isFormValid = false
                getViewBinding().etProductPrice.error = getString(R.string.product_price_must_be_filled)
            }

            else -> {
                isFormValid = true
            }
        }
        return isFormValid
    }

    private val currencyTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.toString() != "") {
                getViewBinding().etProductPrice.removeTextChangedListener(this)

                val parsed = formatRupiah(s.toString())

                getViewBinding().etProductPrice.setText(parsed)
                getViewBinding().etProductPrice.setSelection(parsed.length)
                getViewBinding().etProductPrice.addTextChangedListener(this)
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

}
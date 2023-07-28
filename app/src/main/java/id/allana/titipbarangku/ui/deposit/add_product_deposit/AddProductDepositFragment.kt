package id.allana.titipbarangku.ui.deposit.add_product_deposit

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.databinding.FragmentAddProductDepositBinding
import id.allana.titipbarangku.ui.deposit.DepositViewModel
import id.allana.titipbarangku.ui.deposit.add_product_deposit.adapter.AddProductDepositAdapter
import id.allana.titipbarangku.ui.product.ProductViewModel


class AddProductDepositFragment : BaseFragment<FragmentAddProductDepositBinding>(FragmentAddProductDepositBinding::inflate) {

    private val viewModel: DepositViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val addProductDepositAdapter by lazy {
        AddProductDepositAdapter { itemProductDeposit ->
            showAlertDialog(itemProductDeposit)
        }
    }

    private var productName = arrayListOf<String>()
    private var listProduct = listOf<ProductModel>()
    private var productId = 0
    private var idDeposit = 0L

    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerProduct: AutoCompleteTextView

    private val args by navArgs<AddProductDepositFragmentArgs>()

    override fun initView() {
        idDeposit = args.idDeposit
        initRecyclerView()

        spinnerProduct = getViewBinding().textDropdownProduct
        spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            productName
        )
        spinnerProduct.setAdapter(spinnerAdapter)

        productViewModel.getAllProduct().observe(viewLifecycleOwner) { list ->
            listProduct = list
            for (data in listProduct) {
                productName.add(data.name)
            }
            spinnerAdapter.notifyDataSetChanged()
        }
        spinnerProduct.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedProduct = listProduct[position]
            productId = selectedProduct.id
        }

        getViewBinding().btnAddProduct.setOnClickListener {
            addProduct(idDeposit, productId)
        }

        getViewBinding().btnFinish.setOnClickListener {

        }

        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    findNavController().navigateUp()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initRecyclerView() {
        getViewBinding().rvProductInDeposit.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@AddProductDepositFragment.addProductDepositAdapter
        }
    }

    private fun showAlertDialog(data: ProductDepositWithProduct) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete_data))
            setMessage(getString(R.string.msg_delete_data, data.product?.name))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteProductDeposit(data.productDeposit)
                Snackbar.make(requireView(), getString(R.string.success_delete_data, data.product?.name), Snackbar.LENGTH_SHORT).show()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun addProduct(idDeposit: Long, idProduct: Int) {
        if (idProduct == 0) {
            Snackbar.make(requireView(), getString(R.string.msg_product_quantity_must_be_filled), Snackbar.LENGTH_SHORT).show()
        } else {
            val productDeposit = ProductDepositModel(
                0,
                idDeposit = idDeposit,
                idProduct = productId,
                quantity = getViewBinding().etProductQuantity.text.toString().toInt(),
                returnQuantity = 0
            )
            viewModel.insertProductInDeposit(productDeposit)
            Snackbar.make(requireView(), getString(R.string.success_add_product_in_deposit), Toast.LENGTH_SHORT).show()
        }
    }

    override fun observeData() {
        viewModel.getAllProductInDeposit(idDeposit = idDeposit.toInt()).observe(viewLifecycleOwner) { list ->
            viewModel.checkProductInDeposit(list)
            addProductDepositAdapter.submitList(list)
        }
        viewModel.checkProductInDepositLiveData().observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                stateDataEmpty(true)
            } else {
                stateDataEmpty(false)
            }
        }
    }

    private fun stateDataEmpty(isEmpty: Boolean) {
        getViewBinding().also {
            if (isEmpty) {
                it.ivStateDataEmpty.visibility = View.VISIBLE
                it.tvStateDataEmpty.visibility = View.VISIBLE
            } else {
                it.ivStateDataEmpty.visibility = View.GONE
                it.tvStateDataEmpty.visibility = View.GONE
            }
        }
    }

}
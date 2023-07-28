package id.allana.titipbarangku.ui.deposit.add_product_deposit

import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.databinding.FragmentProductDepositBottomSheetBinding
import id.allana.titipbarangku.ui.deposit.DepositViewModel
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.model.ProductDepositModel


class ProductDepositBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentProductDepositBottomSheetBinding>(FragmentProductDepositBottomSheetBinding::inflate) {

    private val viewModel: DepositViewModel by viewModels()
    private val args by navArgs<ProductDepositBottomSheetFragmentArgs>()

    override fun initView() {
        args.productDepositData?.let { productDepositModel ->
            getViewBinding().etProductQuantity.setText(productDepositModel.quantity)
            getViewBinding().btnUpdateCategory.setOnClickListener {
                updateProductDeposit(productDepositModel)
            }
        }
    }

    private fun updateProductDeposit(data: ProductDepositModel) {
        if (validateForm()) {
            val productDeposit = ProductDepositModel(
                id = data.id,
                idDeposit = data.idDeposit,
                idProduct = data.idProduct,
                quantity = getViewBinding().etProductQuantity.text.toString().toInt(),
                returnQuantity = 0
            )
            viewModel.updateProductDeposit(productDeposit)
            Snackbar.make(requireView(), getString(R.string.success_update_product_deposit), Snackbar.LENGTH_SHORT).show().also { this.dismiss() }
        }
    }

    private fun validateForm(): Boolean {
        val textQuantity = getViewBinding().etProductQuantity.text.toString()
        val isFormValid: Boolean

        if (textQuantity.isEmpty()) {
            isFormValid = false
            getViewBinding().etProductQuantity.error = getString(R.string.msg_total_product_empty)
        } else isFormValid = true
        return isFormValid
    }

}
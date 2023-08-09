package id.allana.titipbarangku.ui.store

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.databinding.FragmentStoreBottomSheetBinding
import id.allana.titipbarangku.util.snackbar


class StoreBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentStoreBottomSheetBinding>(FragmentStoreBottomSheetBinding::inflate) {

    private val viewModel: StoreViewModel by viewModels()
    private val args by navArgs<StoreBottomSheetFragmentArgs>()

    private var idStore = 0

    override fun initView() {
        args.storeData?.let { storeModel ->
            idStore = storeModel.id
            setDataToView(storeModel)
            getViewBinding().btnAddStore.text = getString(R.string.update)
            getViewBinding().btnAddStore.setOnClickListener {
                insertStore(idStore)
            }
        }
        getViewBinding().btnAddStore.setOnClickListener {
            insertStore(idStore)
        }
    }

    private fun insertStore(id: Int) {
        if (validateForm()) {
            val store = StoreModel(
                id = idStore,
                name = getViewBinding().etStoreName.text.toString().trim(),
                address = getViewBinding().etStoreAddress.text.toString().trim(),
                ownerName = getViewBinding().etStoreOwnerName.text.toString().trim(),
                ownerPhoneNumber = getViewBinding().etStorePhoneNumber.text.toString().trim()
            )

            val successMessage = if (id == 0) {
                viewModel.insertStore(store)
                getString(R.string.success_add_store)
            } else {
                viewModel.updateStore(store)
                getString(R.string.success_update_store)
            }

            requireActivity().findViewById<View>(android.R.id.content).snackbar(successMessage, R.id.fab_add_store).also { this.dismiss() }
        }
    }

    private fun setDataToView(data: StoreModel) {
        getViewBinding().apply {
            etStoreName.setText(data.name)
            etStoreAddress.setText(data.address)
            etStoreOwnerName.setText(data.ownerName)
            etStorePhoneNumber.setText(data.ownerPhoneNumber)
        }
    }

    private fun validateForm(): Boolean {
        val textName = getViewBinding().etStoreName.text.toString().trim()
        val textAddress = getViewBinding().etStoreAddress.text.toString().trim()
        val textOwnerName = getViewBinding().etStoreOwnerName.text.toString().trim()
        val textOwnerPhoneNumber = getViewBinding().etStorePhoneNumber.text.toString().trim()

        val isFormValid: Boolean

        when {
            textName.isEmpty() -> {
                isFormValid = false
                getViewBinding().etStoreName.error = "Nama toko harus diisi!"
            }
            textAddress.isEmpty() -> {
                isFormValid = false
                getViewBinding().etStoreAddress.error = "Alamat toko harus diisi!"
            }
            textOwnerName.isEmpty() -> {
                isFormValid = false
                getViewBinding().etStoreOwnerName.error = "Nama pemilik toko harus diisi!"
            }
            textOwnerPhoneNumber.isEmpty() -> {
                isFormValid = false
                getViewBinding().etStorePhoneNumber.error = "Nomor HP pemilik harus diisi!"
            }
            else -> {
                isFormValid = true
            }
        }
        return isFormValid
    }

}
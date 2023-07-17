package id.allana.titipbarangku.ui.store

import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.databinding.FragmentStoreBottomSheetBinding


class StoreBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentStoreBottomSheetBinding>(FragmentStoreBottomSheetBinding::inflate) {

    private val viewModel: StoreViewModel by viewModels()

    override fun initView() {
        getViewBinding().btnAddStore.setOnClickListener {
            insertStore()
        }
    }

    private fun insertStore() {
        if (validateForm()) {
            val store = StoreModel(
                name = getViewBinding().etStoreName.text.toString().trim(),
                address = getViewBinding().etStoreAddress.text.toString().trim(),
                ownerName = getViewBinding().etStoreOwnerName.text.toString().trim(),
                ownerPhoneNumber = getViewBinding().etStorePhoneNumber.text.toString().trim()
            )
            viewModel.insertStore(store)
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Berhasil tambah toko", Snackbar.LENGTH_SHORT).show().also {
                this@StoreBottomSheetFragment.dismiss()
            }
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
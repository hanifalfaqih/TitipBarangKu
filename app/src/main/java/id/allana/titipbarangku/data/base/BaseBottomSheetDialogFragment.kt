package id.allana.titipbarangku.data.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<B : ViewBinding>(
    val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> B
) : BottomSheetDialogFragment() {

    private var _binding: B? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingFactory(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun initView()

    fun getViewBinding(): B = binding

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package id.allana.titipbarangku.data.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B: ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B
): AppCompatActivity() {

    private var _binding: B? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        initView()
        observeData()
    }

    abstract fun initView()

    abstract fun observeData()

    fun getViewBinding(): B = binding

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
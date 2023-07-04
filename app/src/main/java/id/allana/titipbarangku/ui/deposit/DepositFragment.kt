package id.allana.titipbarangku.ui.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.allana.titipbarangku.databinding.FragmentDepositBinding

class DepositFragment : Fragment() {

    private var _binding: FragmentDepositBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val depositViewModel =
            ViewModelProvider(this).get(DepositViewModel::class.java)

        _binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
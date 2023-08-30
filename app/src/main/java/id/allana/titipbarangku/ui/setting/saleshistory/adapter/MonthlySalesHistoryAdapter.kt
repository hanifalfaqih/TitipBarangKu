package id.allana.titipbarangku.ui.setting.saleshistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.databinding.ItemSalesEachMonthBinding
import id.allana.titipbarangku.util.formatRupiah
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthlySalesHistoryAdapter: RecyclerView.Adapter<MonthlySalesHistoryAdapter.MonthlySalesHistoryViewHolder>() {

    var data = mapOf<Int, Int>()

    inner class MonthlySalesHistoryViewHolder(private val binding: ItemSalesEachMonthBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(month: Int, totalAmount: Int) {
            binding.also {
                val monthName = getMonthName(month)

                // Mengatur teks pada elemen tampilan untuk bulan dan total penjualan yang sesuai
                binding.tvCurrentMonth.text = monthName
                binding.tvTotalAmountSales.text = formatRupiah(totalAmount.toString())
            }
        }
    }

    private fun getMonthName(month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        return SimpleDateFormat("MMMM", Locale("id", "ID")).format(calendar.time)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthlySalesHistoryAdapter.MonthlySalesHistoryViewHolder {
        val binding = ItemSalesEachMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthlySalesHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MonthlySalesHistoryAdapter.MonthlySalesHistoryViewHolder,
        position: Int
    ) {
        val month = data.keys.elementAt(position)
        val totalAmount = data[month]
        totalAmount?.let { holder.bind(month, it) }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
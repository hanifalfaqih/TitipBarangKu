package id.allana.titipbarangku.ui.deposit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductDepositWithProduct
import id.allana.titipbarangku.databinding.ItemProductInDetailDepositBinding

class DetailDepositAdapter :
    ListAdapter<ProductDepositWithProduct, DetailDepositAdapter.DetailDepositViewHolder>(
        DetailDepositComparator()
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class DetailDepositViewHolder(private val binding: ItemProductInDetailDepositBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductDepositWithProduct) {
            binding.also {
                val id = data.productDeposit.id
                val idDeposit = data.productDeposit.idDeposit
                val idProduct = data.productDeposit.idProduct
                val quantity = data.productDeposit.quantity
                var returnQuantity = data.productDeposit.returnQuantity
                var totalProductSold = data.productDeposit.totalProductSold

                it.tvProductName.text = data.product?.name
                it.tvProductQuantity.text = "Jumlah Barang = $quantity"
                it.tvProductReturnQuantity.text = "Jumlah Barang Kembali = $returnQuantity"
                it.tvTotalProductSold.text = "Total Barang Terjual  = $totalProductSold"

                it.btnExpand.setOnClickListener {
                    data.productDeposit.isExpanded = !data.productDeposit.isExpanded
                    if (data.productDeposit.isExpanded) {
                        binding.btnExpand.setImageResource(R.drawable.ic_arrow_up)
                        binding.layoutReturnQuantity.visibility = View.VISIBLE
                    } else {
                        binding.btnExpand.setImageResource(R.drawable.ic_arrow_down)
                        binding.layoutReturnQuantity.visibility = View.GONE
                    }
                }


                it.btnUpdateReturnQuantity.setOnClickListener {
                    val valueReturnQuantity = binding.etReturnQuantity.text.toString()
                    returnQuantity = if (valueReturnQuantity.isNullOrEmpty()) 0 else valueReturnQuantity.toInt()
                    data.productDeposit.returnQuantity = returnQuantity

                    // Calculate total product sold and update the UI
                    totalProductSold = data.productDeposit.quantity - returnQuantity
                    data.productDeposit.totalProductSold = totalProductSold

                    val productDeposit = ProductDepositModel(
                        id = id,
                        idDeposit = idDeposit,
                        idProduct = idProduct,
                        quantity = quantity,
                        returnQuantity = returnQuantity,
                        totalProductSold = totalProductSold
                    )
                    onItemClickCallback.onButtonUpdateQuantity(productDeposit)
                }
            }
        }
    }

    class DetailDepositComparator : DiffUtil.ItemCallback<ProductDepositWithProduct>() {
        override fun areItemsTheSame(
            oldItem: ProductDepositWithProduct,
            newItem: ProductDepositWithProduct
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductDepositWithProduct,
            newItem: ProductDepositWithProduct
        ): Boolean {
            return oldItem.productDeposit.id == newItem.productDeposit.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailDepositAdapter.DetailDepositViewHolder {
        val binding = ItemProductInDetailDepositBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailDepositViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetailDepositAdapter.DetailDepositViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onButtonUpdateQuantity(data: ProductDepositModel)
    }
}
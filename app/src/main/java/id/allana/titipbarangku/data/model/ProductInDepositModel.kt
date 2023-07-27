package id.allana.titipbarangku.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_in_deposit_model")
data class ProductInDepositModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_product_deposit")
    val id: Int,
    @ColumnInfo(name = "id_deposit")
    val idDeposit: Long,
    @ColumnInfo(name = "id_product")
    val idProduct: Int,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "return_quantity")
    val returnQuantity: Int
)
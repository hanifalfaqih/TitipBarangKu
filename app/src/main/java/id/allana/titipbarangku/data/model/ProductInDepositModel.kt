package id.allana.titipbarangku.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_in_deposit_model")
data class ProductInDepositModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_deposit_product")
    val id: Int,
    @ColumnInfo(name = "id_deposit")
    val idDeposit: Int,
    @ColumnInfo(name = "id_product")
    val idProducts: Int,
    @ColumnInfo(name = "quantity")
    val quantities: Int,
    @ColumnInfo(name = "return_quantity")
    val returnQuantities: Int
)
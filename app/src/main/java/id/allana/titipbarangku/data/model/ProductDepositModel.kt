package id.allana.titipbarangku.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "product_in_deposit_model", foreignKeys = [
    ForeignKey(
        entity = ProductModel::class,
        parentColumns = ["id_product"],
        childColumns = ["id_product"],
        onDelete = ForeignKey.CASCADE
    )
])
@Parcelize
data class ProductDepositModel(
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
    var returnQuantity: Int,
    @ColumnInfo(name = "total_product_sold")
    var totalProductSold: String = "-",
    @ColumnInfo(name = "is_expanded")
    var isExpanded: Boolean = false
): Parcelable
package id.allana.titipbarangku.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("product_model", foreignKeys = [
    ForeignKey(
        entity = CategoryModel::class,
        parentColumns = ["id_category"],
        childColumns = ["id_category"],
        onDelete = ForeignKey.CASCADE
    )
], indices = [Index("id_category")] // Tambahkan anotasi @Index untuk membuat index pada kolom id_category
)
@Parcelize
data class ProductModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_product")
    val id: Int,
    @ColumnInfo("id_category")
    val idCategory: Int,
    @ColumnInfo("product_name")
    val name: String,
    @ColumnInfo("product_price")
    val price: Int
): Parcelable
package id.allana.titipbarangku.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("product_model")
data class ProductModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idCategory: Int,
    val name: String,
    val price: String
)
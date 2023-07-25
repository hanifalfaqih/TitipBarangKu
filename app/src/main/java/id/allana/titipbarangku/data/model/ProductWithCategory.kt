package id.allana.titipbarangku.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithCategory(
    @Embedded val product: ProductModel,
    @Relation(
        parentColumn = "id_category",
        entityColumn = "id_category"
    )
    val category: CategoryModel? = null
)
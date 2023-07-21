package id.allana.titipbarangku.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithCategory(
    @Embedded val product: ProductModel,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "id"
    )
    val category: CategoryModel? = null
)
package id.allana.titipbarangku.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProductInDepositWithProductModel(
    @Embedded val productDeposit: ProductInDepositModel,
    @Relation(
        parentColumn = "id_product",
        entityColumn = "id_product"
    )
    val product: ProductModel? = null
)
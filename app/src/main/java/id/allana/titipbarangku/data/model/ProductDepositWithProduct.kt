package id.allana.titipbarangku.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProductDepositWithProduct(
    @Embedded val productDeposit: ProductDepositModel,
    @Relation(
        parentColumn = "id_product",
        entityColumn = "id_product"
    )
    val product: ProductModel? = null
)
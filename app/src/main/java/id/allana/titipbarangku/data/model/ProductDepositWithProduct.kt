package id.allana.titipbarangku.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity(tableName = "product_deposit_with_product_model")
data class ProductDepositWithProduct(
    @Embedded val productDeposit: ProductDepositModel,
    @Relation(
        parentColumn = "id_product",
        entityColumn = "id_product"
    )
    val product: ProductModel? = null
)
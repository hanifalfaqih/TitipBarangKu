package id.allana.titipbarangku.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class DepositWithStore(
    @Embedded val deposit: DepositModel,
    @Relation(
        parentColumn = "id_store",
        entityColumn = "id_store"
    )
    val store: StoreModel? = null
)
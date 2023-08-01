package id.allana.titipbarangku.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class DepositWithStore(
    @Embedded val deposit: DepositModel,
    @Relation(
        parentColumn = "id_store",
        entityColumn = "id_store"
    )
    val store: StoreModel? = null
): Parcelable
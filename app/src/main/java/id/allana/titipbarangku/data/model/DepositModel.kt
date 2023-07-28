package id.allana.titipbarangku.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_model", foreignKeys = [
    ForeignKey(
        entity = StoreModel::class,
        parentColumns = ["id_store"],
        childColumns = ["id_store"],
        onDelete = ForeignKey.CASCADE
    )
], indices = [Index("id_store")] // Tambahkan anotasi @Index untuk membuat index pada kolom id_category
)
data class DepositModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_deposit")
    val id: Long,
    @ColumnInfo("id_store")
    val idStore: Int,
    @ColumnInfo("start_date_deposit")
    val startDateDeposit: String,
    @ColumnInfo("finish_date_deposit")
    val finishDateDeposit: String,
    @ColumnInfo("status")
    val status: Status

)

enum class Status{
    DEPOSIT,
    FINISH
}
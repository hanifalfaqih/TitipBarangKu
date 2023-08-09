package id.allana.titipbarangku.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "store_model")
@Parcelize
data class StoreModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_store")
    val id: Int,
    @ColumnInfo("store_name")
    val name: String,
    @ColumnInfo("store_address")
    val address: String,
    @ColumnInfo("store_owner_name")
    val ownerName: String,
    @ColumnInfo("store_owner_phone_number")
    val ownerPhoneNumber: String
): Parcelable

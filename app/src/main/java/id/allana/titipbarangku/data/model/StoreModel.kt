package id.allana.titipbarangku.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "store_model")
@Parcelize
data class StoreModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val ownerName: String,
    val ownerPhoneNumber: String
): Parcelable

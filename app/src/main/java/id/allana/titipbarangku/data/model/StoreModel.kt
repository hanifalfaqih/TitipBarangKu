package id.allana.titipbarangku.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_model")
data class StoreModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val ownerName: String,
    val ownerPhoneNumber: String
)

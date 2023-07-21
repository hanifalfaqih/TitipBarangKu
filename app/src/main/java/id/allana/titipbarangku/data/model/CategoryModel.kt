package id.allana.titipbarangku.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_model")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val categoryName: String
)
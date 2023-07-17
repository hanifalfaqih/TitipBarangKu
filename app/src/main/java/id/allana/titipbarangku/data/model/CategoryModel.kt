package id.allana.titipbarangku.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryName: String
)
package id.allana.titipbarangku.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "category_model")
@Parcelize
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_category")
    val id: Int,
    @ColumnInfo("category_name")
    val categoryName: String
): Parcelable
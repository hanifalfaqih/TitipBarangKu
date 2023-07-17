package id.allana.titipbarangku.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.StoreModel

@Dao
interface ConsignmentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStore(store: StoreModel)


}
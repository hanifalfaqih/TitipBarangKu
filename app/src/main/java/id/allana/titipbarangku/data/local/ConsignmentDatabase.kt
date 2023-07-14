package id.allana.titipbarangku.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.allana.titipbarangku.data.model.CategoryModel

@Database(entities = [CategoryModel::class], version = 1, exportSchema = false)
abstract class ConsignmentDatabase: RoomDatabase() {

    abstract fun consignmentDao(): ConsignmentDao

    companion object {
        @Volatile
        private var INSTANCE: ConsignmentDatabase? = null

        fun getDatabase(context: Context): ConsignmentDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConsignmentDatabase::class.java,
                    "consignment_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
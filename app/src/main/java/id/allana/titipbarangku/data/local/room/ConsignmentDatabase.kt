package id.allana.titipbarangku.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.StatusConverter
import id.allana.titipbarangku.data.model.StoreModel

@Database(entities = [CategoryModel::class, StoreModel::class, ProductModel::class, DepositModel::class, ProductDepositModel::class], version = 1, exportSchema = false)
@TypeConverters(StatusConverter::class)
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
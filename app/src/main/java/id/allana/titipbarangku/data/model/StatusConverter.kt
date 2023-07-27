package id.allana.titipbarangku.data.model

import androidx.room.TypeConverter

class StatusConverter {

    @TypeConverter
    fun fromStatus(status: Status): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(status: String): Status {
        return Status.valueOf(status)
    }
}
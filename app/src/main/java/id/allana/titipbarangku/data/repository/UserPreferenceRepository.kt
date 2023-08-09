package id.allana.titipbarangku.data.repository

import id.allana.titipbarangku.data.local.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserPreferenceRepository(
    private val userPref: UserPreferences
) {
    fun getUserFirstTimeOpenApp(): Flow<Boolean> = userPref.getUserFirstTimeOpenApp()
    suspend fun setUserFirstTimeOpenApp(isFirstTimeOpen: Boolean) {
        userPref.setUserFirstTimeOpenApp(isFirstTimeOpen)
    }

    fun getUserStoreName(): Flow<String> = userPref.getUserStoreName()
    suspend fun setUserStoreName(userStoreName: String) {
        userPref.setUserStoreName(userStoreName)
    }
}
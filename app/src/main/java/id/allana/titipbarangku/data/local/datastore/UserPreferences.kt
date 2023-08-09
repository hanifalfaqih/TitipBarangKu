package id.allana.titipbarangku.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_pref")

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    private val INTRO_KEY = booleanPreferencesKey("intro_key")
    private val STORE_NAME_KEY = stringPreferencesKey("store_name_key")

    fun getUserFirstTimeOpenApp(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[INTRO_KEY] ?: true
        }
    }

    suspend fun setUserFirstTimeOpenApp(isFirstTimeOpen: Boolean) {
        dataStore.edit { preferences ->
            preferences[INTRO_KEY] = isFirstTimeOpen
        }
    }

    fun getUserStoreName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[STORE_NAME_KEY] ?: ""
        }
    }

    suspend fun setUserStoreName(userStoreName: String) {
        dataStore.edit { preferences ->
            preferences[STORE_NAME_KEY] = userStoreName
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
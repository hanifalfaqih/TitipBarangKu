package id.allana.titipbarangku.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_pref")

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    private val INTRO_KEY = booleanPreferencesKey("intro_key")
    private val STORE_NAME_KEY = stringPreferencesKey("store_name_key")
    private val AUTH_KEY = stringPreferencesKey("auth_key")
    private val APP_LANGUAGE_KEY = stringPreferencesKey("app_language_key")
    private val LAST_DATE_BACKUP_KEY = stringPreferencesKey("last_date_backup_key")

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

    fun getAuthUser(context: Context): String {
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedPreferences = EncryptedSharedPreferences.create(
            context,
            "encrypted_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return encryptedPreferences.getString(AUTH_KEY.name, null) ?: ""
    }

    fun setAuthUser(auth: String, context: Context) {
        val masterKeyAlias = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        val encryptedPreferences = EncryptedSharedPreferences.create(
            context,
            "encrypted_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        encryptedPreferences.edit().putString(AUTH_KEY.name, auth).apply()
    }

    fun getAppLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[APP_LANGUAGE_KEY] ?: ""
        }
    }
    suspend fun setAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[APP_LANGUAGE_KEY] = language
        }
    }

    fun getLastDateBackup(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LAST_DATE_BACKUP_KEY] ?: ""
        }
    }
    suspend fun setLastDateBackup(date: String) {
        dataStore.edit { preferences ->
            preferences[LAST_DATE_BACKUP_KEY] = date
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
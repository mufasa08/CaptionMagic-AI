package com.devinjapan.aisocialmediaposter.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.devinjapan.aisocialmediaposter.data.util.Constants.DATASTORE_NAME
import com.devinjapan.aisocialmediaposter.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DatastoreRepository {
    override suspend fun putList(key: String, value: List<String>) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = value.filter { it.isEmpty() }.joinToString("####")
        }
    }

    override suspend fun getList(key: String): List<String> {
        return try {
            val preferenceKey = stringPreferencesKey(key)
            val preference = context.dataStore.data.first()
            preference[preferenceKey]?.split("####") ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun putString(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun putLong(key: String, value: Long) {
        val preferenceKey = longPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferenceKey = stringPreferencesKey(key)
            val preference = context.dataStore.data.first()
            preference[preferenceKey]
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getStringFlow(key: String): Flow<String> {

        val preferenceKey = stringPreferencesKey(key)
        val data = context.dataStore.data
        return data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preference ->
                preference[preferenceKey] ?: ""
            }.distinctUntilChanged()
    }

    override suspend fun getBooleanFlow(key: String): Flow<Boolean> {
        val preferenceKey = booleanPreferencesKey(key)
        val data = context.dataStore.data
        return data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preference ->
                preference[preferenceKey] ?: false
            }.distinctUntilChanged()
    }

    override suspend fun getLong(key: String): Long? {
        return try {
            val preferenceKey = longPreferencesKey(key)
            val preference = context.dataStore.data.first()
            preference[preferenceKey]
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getLongFlow(key: String): Flow<Long?> {

        val preferenceKey = longPreferencesKey(key)
        val data = context.dataStore.data
        return data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preference ->
                preference[preferenceKey]
            }.distinctUntilChanged()
    }

    override suspend fun clearPreferences(key: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            if (it.contains(preferenceKey)) {
                it.remove(preferenceKey)
            }
        }
    }
}

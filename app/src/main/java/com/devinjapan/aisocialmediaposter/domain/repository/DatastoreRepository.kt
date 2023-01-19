package com.devinjapan.aisocialmediaposter.domain.repository

import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {

    suspend fun putList(key: String, value: List<String>)
    suspend fun getList(key: String): List<String>
    suspend fun putString(key: String, value: String)
    suspend fun putLong(key: String, value: Long)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getString(key: String): String?
    suspend fun getStringFlow(key: String): Flow<String>
    suspend fun getBooleanFlow(key: String): Flow<Boolean>
    suspend fun getLong(key: String): Long?
    suspend fun getLongFlow(key: String): Flow<Long?>
    suspend fun clearPreferences(key: String)
}

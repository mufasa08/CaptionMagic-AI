package com.devinjapan.aisocialmediaposter.shared.domain.repository

interface DataStoreRepository {

    suspend fun putList(key: String, value: List<String>)
    suspend fun getList(key: String): List<String>
    suspend fun putString(key: String, value: String)
    suspend fun putLong(key: String, value: Long)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getString(key: String): String?
    suspend fun getLong(key: String): Long?
    suspend fun clearPreferences(key: String)
    suspend fun getBoolean(key: String): Boolean?
}

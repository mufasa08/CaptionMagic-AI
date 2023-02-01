package com.devinjapan.aisocialmediaposter.shared.data.repository

import com.devinjapan.aisocialmediaposter.shared.domain.repository.DataStoreRepository
import com.russhwolf.settings.Settings

class DataStoreRepositoryImpl(private val settings: Settings) : DataStoreRepository {
    override suspend fun putList(key: String, value: List<String>) {
        if (value.isNotEmpty()) {
            val stringList = value.joinToString("####")
            settings.putString(key, stringList)
        }
    }

    override suspend fun getList(key: String): List<String> {
        val list = settings.getString(key, "").split("####")
        return list
    }

    override suspend fun putString(key: String, value: String) {
        settings.putString(key, value)
    }

    override suspend fun putLong(key: String, value: Long) {
        settings.putLong(key, value)
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    override suspend fun getString(key: String): String {
        return settings.getString(key, "")
    }

    override suspend fun getLong(key: String): Long {
        return settings.getLong(key, 1L)
    }

    override suspend fun clearPreferences(key: String) {
        settings.remove(key)
    }

    override suspend fun getBoolean(key: String): Boolean {
        return settings.getBoolean(key, false)
    }
}

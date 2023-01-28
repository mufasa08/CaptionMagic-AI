package com.devinjapan.shared.data.repository

import com.devinjapan.shared.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class DataStoreRepositoryImpl : DataStoreRepository {
    override suspend fun putList(key: String, value: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun getList(key: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun putString(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override suspend fun putLong(key: String, value: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getString(key: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getStringFlow(key: String): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getBooleanFlow(key: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getLong(key: String): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getLongFlow(key: String): Flow<Long?> {
        TODO("Not yet implemented")
    }

    override suspend fun clearPreferences(key: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getBoolean(key: String): Boolean? {
        TODO("Not yet implemented")
    }
}

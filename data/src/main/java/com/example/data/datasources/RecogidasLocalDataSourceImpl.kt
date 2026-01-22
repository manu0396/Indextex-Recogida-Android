package com.example.data.datasources

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.data.db.RecogidaDatabase
import com.example.data.db.AuthorizedEntity
import kotlinx.coroutines.withContext

class RecogidasLocalDataSourceImpl(
    database: RecogidaDatabase,
    private val dispatchers: DispatcherProvider
) : RecogidasLocalDataSource {

    private val queries = database.recogidaQueries

    override suspend fun getPersonnelByQr(qr: String): AuthorizedEntity? {
        return withContext(dispatchers.io) {
            queries.getPersonnelByQr(qr).executeAsOneOrNull()
        }
    }

    override suspend fun savePersonnel(entity: AuthorizedEntity) {
        withContext(dispatchers.io) {
            queries.insertUser(entity)
        }
    }

    override suspend fun clearAndSaveAll(entities: List<AuthorizedEntity>) {
        withContext(dispatchers.io) {
            queries.transaction {
                queries.clearAll()
                entities.forEach { entity ->
                    queries.insertUser(entity)
                }
            }
        }
    }
}

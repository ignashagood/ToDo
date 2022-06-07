package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.CatalogDAO
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.relations.CatalogWithTasks
import nktns.todo.data.database.subset.CatalogWithCounts
import java.util.Date

class CatalogRepository(private val catalogDAO: CatalogDAO) {

    fun get(id: Int): CatalogEntity? = catalogDAO.get(id)

    suspend fun getWithTasks(id: Int): CatalogWithTasks? = catalogDAO.getWithTasks(id)

    fun getAll(): Flow<List<CatalogEntity>> = catalogDAO.getAll()

    fun getAllWithCounts(): Flow<List<CatalogWithCounts>> = catalogDAO.getAllWithCounts(Date())

    suspend fun add(catalog: CatalogEntity) {
        catalogDAO.add(catalog)
    }

    suspend fun update(catalog: CatalogEntity) {
        catalogDAO.update(catalog)
    }

    suspend fun delete(catalog: CatalogEntity) {
        catalogDAO.delete(catalog)
    }
}

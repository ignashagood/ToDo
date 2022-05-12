package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.CatalogDAO
import nktns.todo.data.database.entity.Catalog
import nktns.todo.data.database.relations.CatalogWithTasks
import nktns.todo.data.database.subset.CatalogWithCounts

class CatalogRepository(private val catalogDAO: CatalogDAO) {

    fun get(catalogId: Int): Catalog {
        return catalogDAO.get(catalogId)
    }

    fun getAll(): Flow<List<Catalog>> = catalogDAO.getAll()

    fun getWithTasks(id: Int): CatalogWithTasks = catalogDAO.getCatalogWithTasks(id)

    fun getAllWithCounts(): List<CatalogWithCounts> = catalogDAO.getAllWithCounts()

    suspend fun add(catalog: Catalog) {
        catalogDAO.add(catalog)
    }

    suspend fun update(catalog: Catalog) {
        catalogDAO.update(catalog)
    }

    suspend fun delete(catalog: Catalog) {
        catalogDAO.delete(catalog)
    }
}

package nktns.todo.base.database

import kotlinx.coroutines.flow.Flow
import nktns.todo.base.database.dao.CatalogDAO
import nktns.todo.base.database.entity.Catalog

class CatalogRepository(private val catalogDAO: CatalogDAO) {
    val allCatalogs: Flow<List<Catalog>> = catalogDAO.getTaskLists()

    suspend fun add(catalog: Catalog) {
        catalogDAO.add(catalog)
    }

    suspend fun delete(catalog: Catalog) {
        catalogDAO.delete(catalog)
    }

    suspend fun update(catalog: Catalog) {
        catalogDAO.update(catalog)
    }

    fun get(catalogId: Int): Catalog {
        return catalogDAO.get(catalogId)
    }
}

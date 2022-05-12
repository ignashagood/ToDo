package nktns.todo.data

import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.dao.CatalogDAO
import nktns.todo.data.database.entity.Catalog

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

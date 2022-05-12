package nktns.todo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.entity.Catalog
import nktns.todo.data.database.relations.CatalogWithTasks
import nktns.todo.data.database.subset.CatalogWithCounts

@Dao
interface CatalogDAO {
    @Insert
    suspend fun add(catalog: Catalog)

    @Update
    suspend fun update(catalog: Catalog)

    @Delete
    suspend fun delete(catalog: Catalog)

    @Query("SELECT * FROM catalogs WHERE catalogId = :id")
    fun get(id: Int): Catalog

    @Transaction
    @Query("SELECT * FROM catalogs WHERE catalogId = :id")
    fun getWithTasks(id: Int): CatalogWithTasks

    @Query("SELECT * from catalogs ORDER BY creationDate")
    fun getAll(): Flow<List<Catalog>>

    @Query(
        "SELECT *, " +
            "COUNT(taskId) AS taskCount," +
            "COUNT(case when taskCompletionDate < DATE('now') then taskId else null end) AS outdatedTaskCount " +
            "FROM catalogs " +
            "LEFT OUTER JOIN tasks ON taskParentId = catalogId " +
            "GROUP BY taskParentId " +
            "ORDER BY catalogId DESC"
    )
    fun getWithCounts(): List<CatalogWithCounts>
}

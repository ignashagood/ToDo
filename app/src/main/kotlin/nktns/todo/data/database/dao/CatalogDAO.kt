package nktns.todo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.relations.CatalogWithTasks
import nktns.todo.data.database.subset.CatalogWithCounts

@Dao
interface CatalogDAO {

    @Query("SELECT * FROM catalogs WHERE catalogId = :id")
    fun get(id: Int): CatalogEntity?

    @Transaction
    @Query("SELECT * FROM catalogs WHERE catalogId = :id")
    suspend fun getWithTasks(id: Int): CatalogWithTasks?

    @Query("SELECT * from catalogs ORDER BY catalogCreationDate")
    fun getAll(): Flow<List<CatalogEntity>>

    @Query(
        """
        SELECT 
            catalogId, 
            catalogName, 
            catalogCreationDate,
            COUNT(taskId) AS taskCount,
            COUNT(case when taskCompletionDate < DATE('now') then taskId else null end) AS outdatedTaskCount
        FROM catalogs
        LEFT OUTER JOIN tasks ON catalogId = taskCatalogId
        GROUP BY taskCatalogId
        ORDER BY taskCatalogId DESC
        """
    )
    fun getAllWithCounts(): Flow<List<CatalogWithCounts>>

    @Insert
    suspend fun add(catalog: CatalogEntity)

    @Update
    suspend fun update(catalog: CatalogEntity)

    @Delete
    suspend fun delete(catalog: CatalogEntity)
}

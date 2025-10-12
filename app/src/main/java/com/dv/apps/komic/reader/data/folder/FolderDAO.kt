package com.dv.apps.komic.reader.data.folder

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: FolderEntity)

    @Query("SELECT * FROM folders")
    fun all(): Flow<List<FolderEntity>>
}
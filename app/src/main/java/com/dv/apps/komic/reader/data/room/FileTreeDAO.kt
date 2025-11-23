package com.dv.apps.komic.reader.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FileTreeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: FileTreeEntity)

    @Query("SELECT * FROM file_tree")
    fun all(): Flow<List<FileTreeEntity>>
}
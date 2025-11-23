package com.dv.apps.komic.reader.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dv.apps.komic.reader.data.room.FileTreeDAO
import com.dv.apps.komic.reader.data.room.FileTreeEntity

@Database(
    entities = [
        FileTreeEntity::class
    ],
    version = 1
)
abstract class KomicReaderDatabase : RoomDatabase() {
    abstract val fileTreeDAO: FileTreeDAO
}
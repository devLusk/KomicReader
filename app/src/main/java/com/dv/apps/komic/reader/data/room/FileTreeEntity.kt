package com.dv.apps.komic.reader.data.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    "file_tree",
    indices = [
        Index("path", unique = true)
    ]
)
data class FileTreeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val path: String
)
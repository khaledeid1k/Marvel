package com.chocolatecake.marvel.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SEARCH_HISTORY_TABLE" )
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val keyword: String,
    val type: String,
)

package com.example.catlendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "events")
data class CatEvent(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val time: String,
    val date: String // Storing as String to simplify Room DB queries, alternatively we can use TypeConverters
)

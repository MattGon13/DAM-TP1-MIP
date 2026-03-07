package com.example.catlendar.model

import java.time.LocalDate
import java.util.UUID

data class CatEvent(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val time: String,
    val date: LocalDate
)

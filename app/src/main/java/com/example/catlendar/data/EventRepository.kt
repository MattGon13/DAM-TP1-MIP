package com.example.catlendar.data

import com.example.catlendar.model.CatEvent
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    val allEvents: Flow<List<CatEvent>> = eventDao.getAllEvents()

    fun getEventsByDate(date: String): Flow<List<CatEvent>> {
        return eventDao.getEventsByDate(date)
    }

    fun searchEvents(query: String): Flow<List<CatEvent>> {
        return eventDao.searchEvents(query)
    }

    suspend fun insertEvent(event: CatEvent) {
        eventDao.insertEvent(event)
    }

    suspend fun deleteEvent(eventId: String) {
        eventDao.deleteEvent(eventId)
    }
}

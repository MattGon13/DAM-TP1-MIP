package com.example.catlendar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catlendar.model.CatEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<CatEvent>>

    @Query("SELECT * FROM events WHERE date = :date")
    fun getEventsByDate(date: String): Flow<List<CatEvent>>

    @Query("SELECT * FROM events WHERE title LIKE '%' || :searchQuery || '%'")
    fun searchEvents(searchQuery: String): Flow<List<CatEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CatEvent)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: String)
}

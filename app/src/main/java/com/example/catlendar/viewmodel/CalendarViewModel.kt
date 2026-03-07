package com.example.catlendar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.catlendar.model.CatEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val eventsForSelectedDate: List<CatEvent> = emptyList(),
    val allEvents: List<CatEvent> = emptyList() // Simulating a local DB
)

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    fun selectDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date,
                eventsForSelectedDate = currentState.allEvents.filter { it.date == date }
            )
        }
    }

    fun addEvent(title: String, time: String) {
        val newEvent = CatEvent(
            title = title,
            time = time,
            date = _uiState.value.selectedDate
        )
        
        _uiState.update { currentState ->
            val updatedAllEvents = currentState.allEvents + newEvent
            currentState.copy(
                allEvents = updatedAllEvents,
                eventsForSelectedDate = updatedAllEvents.filter { it.date == currentState.selectedDate }
            )
        }
    }

    fun deleteEvent(eventId: String) {
        _uiState.update { currentState ->
            val updatedAllEvents = currentState.allEvents.filterNot { it.id == eventId }
            currentState.copy(
                allEvents = updatedAllEvents,
                eventsForSelectedDate = updatedAllEvents.filter { it.date == currentState.selectedDate }
            )
        }
    }
}

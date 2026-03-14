package com.example.catlendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlendar.data.EventRepository
import com.example.catlendar.model.CatEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val eventsForSelectedDate: List<CatEvent> = emptyList()
)

class CalendarViewModel(private val repository: EventRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())

    val uiState: StateFlow<CalendarUiState> = combine(
        _selectedDate,
        repository.allEvents 
    ) { date, events ->
        CalendarUiState(
            selectedDate = date,
            eventsForSelectedDate = events.filter { it.date == date.toString() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun addEvent(title: String, time: String) {
        val newEvent = CatEvent(
            title = title,
            time = time,
            date = _selectedDate.value.toString()
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertEvent(newEvent)
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEvent(eventId)
        }
    }
}

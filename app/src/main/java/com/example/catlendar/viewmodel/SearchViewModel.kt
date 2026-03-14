package com.example.catlendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlendar.data.EventRepository
import com.example.catlendar.model.CatEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<CatEvent> = emptyList()
)

class SearchViewModel(private val repository: EventRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SearchUiState> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(SearchUiState(searchQuery = query, searchResults = emptyList()))
            } else {
                repository.searchEvents(query).map { results ->
                    SearchUiState(searchQuery = query, searchResults = results)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEvent(eventId)
        }
    }
}

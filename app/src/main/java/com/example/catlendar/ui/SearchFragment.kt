package com.example.catlendar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catlendar.databinding.FragmentSearchBinding
import com.example.catlendar.viewmodel.CalendarViewModel
import com.example.catlendar.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // Shared with CalendarFragment to get all events if we had a real DB
    // In our case we will just search the simulated DB from CalendarViewModel
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()

    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(
            onDelete = { event ->
                calendarViewModel.deleteEvent(event.id)
                // Trigger an update
                searchViewModel.updateSearchQuery(
                    binding.searchBar.text.toString(),
                    calendarViewModel.uiState.value.allEvents
                )
            }
        )
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchResults.adapter = adapter
    }

    private fun setupSearchBar() {
        binding.searchView.editText.setOnEditorActionListener { v, _, _ ->
            val query = v.text.toString()
            binding.searchBar.setText(query)
            searchViewModel.updateSearchQuery(query, calendarViewModel.uiState.value.allEvents)
            binding.searchView.hide()
            true
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.uiState.collect { state ->
                    adapter.submitList(state.searchResults)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

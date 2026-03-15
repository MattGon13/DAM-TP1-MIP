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
import com.example.catlendar.data.AppDatabase
import com.example.catlendar.data.EventRepository
import com.example.catlendar.databinding.FragmentSearchBinding
import com.example.catlendar.viewmodel.CalendarViewModelFactory
import com.example.catlendar.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import androidx.core.widget.addTextChangedListener

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // Use ViewModelFactory to inject Repository
    private val searchViewModel: SearchViewModel by activityViewModels {
        CalendarViewModelFactory(EventRepository(AppDatabase.getDatabase(requireContext().applicationContext).eventDao()))
    }

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
            showDate = true,
            onDelete = { event ->
                searchViewModel.deleteEvent(event.id)
            }
        )
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchResults.adapter = adapter
    }

    private fun setupSearchBar() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        
        binding.searchView.editText.addTextChangedListener {
            searchViewModel.updateSearchQuery(it.toString())
        }
        
        binding.searchView.editText.setOnEditorActionListener { v, _, _ ->
            val query = v.text.toString()
            binding.searchBar.setText(query)
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

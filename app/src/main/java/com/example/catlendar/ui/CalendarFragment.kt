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
import com.example.catlendar.databinding.DialogAddEventBinding
import com.example.catlendar.databinding.FragmentCalendarBinding
import com.example.catlendar.viewmodel.CalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCalendar()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(
            onDelete = { event ->
                viewModel.deleteEvent(event.id)
            }
        )
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = adapter
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Note: month is 0-indexed in CalendarView
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            viewModel.selectDate(selectedDate)
        }
        
        // Initialize calendar view to the selected date
        val selectedMillis = viewModel.uiState.value.selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        binding.calendarView.date = selectedMillis
    }

    private fun setupFab() {
        binding.fabAddEvent.setOnClickListener {
            showAddEventDialog()
        }
    }

    private fun showAddEventDialog() {
        val dialogBinding = DialogAddEventBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogBinding.root)

        dialogBinding.buttonSaveEvent.setOnClickListener {
            val title = dialogBinding.editTextTitle.text?.toString()?.trim() ?: ""
            val time = dialogBinding.editTextTime.text?.toString()?.trim() ?: ""
            
            if (title.isNotEmpty() && time.isNotEmpty()) {
                viewModel.addEvent(title, time)
                dialog.dismiss()
            } else {
                if (title.isEmpty()) dialogBinding.inputLayoutTitle.error = "Required"
                if (time.isEmpty()) dialogBinding.inputLayoutTime.error = "Required"
            }
        }

        dialog.show()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.eventsForSelectedDate)
                    if (state.eventsForSelectedDate.isEmpty()) {
                        binding.textEmptyState.visibility = View.VISIBLE
                        binding.recyclerViewEvents.visibility = View.GONE
                    } else {
                        binding.textEmptyState.visibility = View.GONE
                        binding.recyclerViewEvents.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

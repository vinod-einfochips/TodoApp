package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.TodoApplication
import com.example.todoapp.R
import com.example.todoapp.data.Todo
import com.example.todoapp.databinding.FragmentAddEditTodoBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditTodoFragment : Fragment() {

    private var _binding: FragmentAddEditTodoBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AddEditTodoViewModel
    private var selectedDueDate: Long? = null
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this, AddEditTodoViewModelFactory(
            activity?.application as TodoApplication,
            arguments?.getInt("todoId", -1) ?: -1
        )).get(AddEditTodoViewModel::class.java)
        
        setupClickListeners()
        setupObservers()
        loadTodoIfEditing()
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            saveTodo()
        }
        
        binding.dueDateLayout.setEndIconOnClickListener {
            showDatePicker()
        }
        
        binding.dueDateInput.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
                if (shouldNavigate) {
                    findNavController().navigateUp()
                    viewModel.onNavigationComplete()
                }
            }
        }
    }

    private fun loadTodoIfEditing() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todo.observe(viewLifecycleOwner) { todo ->
                todo?.let {
                    binding.titleInput.setText(it.title)
                    binding.descriptionInput.setText(it.description)
                    
                    if (it.dueDate != null) {
                        selectedDueDate = it.dueDate
                        binding.dueDateInput.setText(dateFormat.format(Date(it.dueDate)))
                    }
                    
                    when (it.priority) {
                        Todo.Priority.LOW -> binding.lowPriorityRadio.isChecked = true
                        Todo.Priority.MEDIUM -> binding.mediumPriorityRadio.isChecked = true
                        Todo.Priority.HIGH -> binding.highPriorityRadio.isChecked = true
                    }
                }
            }
        }
    }

    private fun saveTodo() {
        val title = binding.titleInput.text.toString().trim()
        val description = binding.descriptionInput.text.toString().trim()
        
        if (title.isEmpty()) {
            binding.titleLayout.error = getString(R.string.title_required)
            return
        }
        
        binding.titleLayout.error = null
        
        val priority = when (binding.priorityRadioGroup.checkedRadioButtonId) {
            binding.lowPriorityRadio.id -> Todo.Priority.LOW
            binding.mediumPriorityRadio.id -> Todo.Priority.MEDIUM
            binding.highPriorityRadio.id -> Todo.Priority.HIGH
            else -> Todo.Priority.MEDIUM
        }
        
        viewModel.saveTodo(
            title = title,
            description = description.ifEmpty { null },
            priority = priority,
            dueDate = selectedDueDate
        )
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        if (selectedDueDate != null) {
            calendar.timeInMillis = selectedDueDate!!
        }
        
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                selectedDueDate = selectedCalendar.timeInMillis
                binding.dueDateInput.setText(dateFormat.format(selectedCalendar.time))
            },
            year,
            month,
            day
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

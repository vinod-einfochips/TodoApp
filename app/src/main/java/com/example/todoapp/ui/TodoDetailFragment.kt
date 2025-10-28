package com.example.todoapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.TodoApplication
import com.example.todoapp.R
import com.example.todoapp.data.Todo
import com.example.todoapp.databinding.FragmentTodoDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TodoDetailFragment : Fragment() {

    private var _binding: FragmentTodoDetailBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: TodoDetailViewModel
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val todoId = arguments?.getInt("todoId", -1) ?: -1
        viewModel = ViewModelProvider(this, TodoDetailViewModelFactory(
            activity?.application as TodoApplication,
            todoId
        )).get(TodoDetailViewModel::class.java)
        
        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.editButton.setOnClickListener {
            viewModel.onEditTodo()
        }
        
        binding.deleteButton.setOnClickListener {
            showDeleteConfirmation()
        }
        
        binding.completeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleCompletion(isChecked)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todo.observe(viewLifecycleOwner) { todo ->
                todo?.let {
                    displayTodo(it)
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToEdit.observe(viewLifecycleOwner) { todoId ->
                todoId?.let {
                    val bundle = Bundle().apply {
                        putInt("todoId", todoId)
                    }
                    findNavController().navigate(R.id.addEditTodoFragment, bundle)
                    viewModel.onEditNavigated()
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
                if (shouldNavigate) {
                    findNavController().navigateUp()
                    viewModel.onNavigationComplete()
                }
            }
        }
    }

    private fun displayTodo(todo: Todo) {
        binding.apply {
            titleText.text = todo.title
            descriptionText.text = todo.description ?: getString(R.string.no_description)
            completeCheckbox.isChecked = todo.isCompleted
            
            priorityChip.text = todo.priority.name
            priorityChip.setChipBackgroundColorResource(
                when (todo.priority) {
                    Todo.Priority.HIGH -> R.color.priority_high
                    Todo.Priority.MEDIUM -> R.color.priority_medium
                    Todo.Priority.LOW -> R.color.priority_low
                }
            )
            
            dueDateText.text = if (todo.dueDate != null) {
                dateFormat.format(Date(todo.dueDate))
            } else {
                getString(R.string.no_due_date)
            }
            
            statusText.text = getString(
                R.string.created_on,
                dateFormat.format(Date(todo.createdAt))
            )
        }
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_todo)
            .setMessage(R.string.delete_confirmation)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteTodo()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

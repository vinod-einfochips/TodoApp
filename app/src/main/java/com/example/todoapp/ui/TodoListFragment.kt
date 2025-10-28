package com.example.todoapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Todo
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: TodoListViewModel
    private lateinit var adapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this).get(TodoListViewModel::class.java)
        
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
        setupSearch()
        
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() {
        adapter = TodoListAdapter(
            onTodoClick = { todo ->
                viewModel.onTodoClicked(todo.id)
            },
            onTodoCheckboxClick = { todo, isChecked ->
                viewModel.toggleTodoCompletion(todo.copy(isCompleted = isChecked))
            }
        )
        
        binding.todoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@TodoListFragment.adapter
            setHasFixedSize(true)
        }
        
        // Add swipe to delete
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val todo = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteTodo(todo)
                
                // Show undo option
                Snackbar.make(
                    binding.root,
                    getString(R.string.todo_deleted, todo.title),
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.undo) {
                    viewModel.insertTodo(todo)
                }.show()
            }
        }).attachToRecyclerView(binding.todoRecyclerView)
    }

    private fun setupClickListeners() {
        // FAB is handled in MainActivity
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.todos.observe(viewLifecycleOwner) { todos ->
                adapter.submitList(todos)
                // Show empty state if no todos
                if (todos.isNullOrEmpty()) {
                    // Show empty state
                } else {
                    // Hide empty state
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToAddTodo.observe(viewLifecycleOwner) { shouldNavigate ->
                if (shouldNavigate) {
                    findNavController().navigate(R.id.addEditTodoFragment)
                    viewModel.onAddNewTodoNavigated()
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToTodoDetail.observe(viewLifecycleOwner) { todoId ->
                todoId?.let {
                    val bundle = Bundle().apply {
                        putInt("todoId", todoId)
                    }
                    findNavController().navigate(R.id.todoDetailFragment, bundle)
                    viewModel.onTodoDetailNavigated()
                }
            }
        }
    }
    
    private fun setupSearch() {
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchInput.text.toString().trim()
                searchTodos(query)
                return@setOnEditorActionListener true
            }
            false
        }
        
        binding.searchLayout.setEndIconOnClickListener {
            binding.searchInput.text?.clear()
            searchTodos("")
        }
    }
    
    private fun searchTodos(query: String) {
        viewModel.searchTodos(query).observe(viewLifecycleOwner) { todos ->
            adapter.submitList(todos)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_todo_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_completed -> {
                viewModel.deleteCompleted()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

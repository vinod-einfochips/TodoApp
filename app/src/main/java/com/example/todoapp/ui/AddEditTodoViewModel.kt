package com.example.todoapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.TodoApplication
import com.example.todoapp.data.Todo
import com.example.todoapp.data.Todo.Priority
import kotlinx.coroutines.launch

class AddEditTodoViewModel(
    application: Application,
    private val todoId: Int = -1
) : AndroidViewModel(application) {
    
    private val repository = (application as TodoApplication).repository
    
    private val _todo = MutableLiveData<Todo?>()
    val todo: LiveData<Todo?> = _todo
    
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack
    
    init {
        if (todoId != -1) {
            viewModelScope.launch {
                _todo.value = repository.getTodoById(todoId)
            }
        }
    }
    
    fun saveTodo(
        title: String,
        description: String?,
        priority: Priority,
        dueDate: Long? = null
    ) {
        val currentTitle = title.trim()
        if (currentTitle.isBlank()) {
            return
        }
        
        val currentTodo = _todo.value
        if (currentTodo != null) {
            // Update existing todo
            val updatedTodo = currentTodo.copy(
                title = currentTitle,
                description = description,
                priority = priority,
                dueDate = dueDate
            )
            updateTodo(updatedTodo)
        } else {
            // Create new todo
            val newTodo = Todo(
                title = currentTitle,
                description = description,
                priority = priority,
                dueDate = dueDate
            )
            insertTodo(newTodo)
        }
    }
    
    private fun insertTodo(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
        _navigateBack.value = true
    }
    
    private fun updateTodo(todo: Todo) = viewModelScope.launch {
        repository.update(todo)
        _navigateBack.value = true
    }
    
    fun onNavigateBack() {
        _navigateBack.value = true
    }
    
    fun onNavigationComplete() {
        _navigateBack.value = false
    }
}

package com.example.todoapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.TodoApplication
import com.example.todoapp.data.Todo
import kotlinx.coroutines.launch

class TodoDetailViewModel(
    application: Application,
    private val todoId: Int
) : AndroidViewModel(application) {
    
    private val repository = (application as TodoApplication).repository
    
    private val _todo = MutableLiveData<Todo?>()
    val todo: LiveData<Todo?> = _todo
    
    private val _navigateToEdit = MutableLiveData<Int?>()
    val navigateToEdit: LiveData<Int?> = _navigateToEdit
    
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack
    
    init {
        loadTodo()
    }
    
    private fun loadTodo() {
        viewModelScope.launch {
            _todo.value = repository.getTodoById(todoId)
        }
    }
    
    fun toggleCompletion(isCompleted: Boolean) {
        _todo.value?.let { todo ->
            viewModelScope.launch {
                repository.update(todo.copy(isCompleted = isCompleted))
                loadTodo()
            }
        }
    }
    
    fun deleteTodo() {
        _todo.value?.let { todo ->
            viewModelScope.launch {
                repository.delete(todo)
                _navigateBack.value = true
            }
        }
    }
    
    fun onEditTodo() {
        _navigateToEdit.value = todoId
    }
    
    fun onEditNavigated() {
        _navigateToEdit.value = null
    }
    
    fun onNavigationComplete() {
        _navigateBack.value = false
    }
}

package com.example.todoapp.ui
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.TodoApplication
import com.example.todoapp.data.Todo
import kotlinx.coroutines.launch

class TodoListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as TodoApplication).repository
    
    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>>
        get() = _todos
    
    private val _navigateToAddTodo = MutableLiveData<Boolean>()
    val navigateToAddTodo: LiveData<Boolean>
        get() = _navigateToAddTodo
    
    private val _navigateToTodoDetail = MutableLiveData<Int>()
    val navigateToTodoDetail: LiveData<Int>
        get() = _navigateToTodoDetail
    
    init {
        repository.allTodos.observeForever { todos ->
            _todos.value = todos
        }
    }
    
    fun onAddNewTodo() {
        _navigateToAddTodo.value = true
    }
    
    fun onAddNewTodoNavigated() {
        _navigateToAddTodo.value = false
    }
    
    fun onTodoClicked(todoId: Int) {
        _navigateToTodoDetail.value = todoId
    }
    
    fun onTodoDetailNavigated() {
        _navigateToTodoDetail.value = null
    }
    
    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
    
    fun toggleTodoCompletion(todo: Todo) {
        viewModelScope.launch {
            val updatedTodo = todo.copy(isCompleted = !todo.isCompleted)
            repository.update(updatedTodo)
        }
    }
    
    fun deleteCompleted() {
        viewModelScope.launch {
            repository.deleteCompleted()
        }
    }
    
    fun insertTodo(todo: Todo) {
        viewModelScope.launch {
            repository.insert(todo)
        }
    }
    
    fun searchTodos(query: String): LiveData<List<Todo>> {
        return repository.searchTodos(query)
    }
}

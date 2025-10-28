package com.example.todoapp.data

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()
    
    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }
    
    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }
    
    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }
    
    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }
    
    fun searchTodos(searchQuery: String): LiveData<List<Todo>> {
        return todoDao.searchTodos(searchQuery)
    }
    
    suspend fun deleteCompleted() {
        todoDao.deleteCompleted()
    }
}

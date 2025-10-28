package com.example.todoapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TodoRepositoryTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var todoDao: TodoDao
    
    private lateinit var repository: TodoRepository
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        // Mock getAllTodos before creating repository since it's called in constructor
        val mockLiveData = MutableLiveData<List<Todo>>()
        whenever(todoDao.getAllTodos()).thenReturn(mockLiveData)
        repository = TodoRepository(todoDao)
    }
    
    @Test
    fun testInsertTodo() = runBlocking {
        // Arrange
        val todo = Todo(
            id = 1,
            title = "Test Todo",
            description = "Test Description",
            priority = Todo.Priority.HIGH
        )
        
        // Act
        repository.insert(todo)
        
        // Assert
        verify(todoDao).insert(todo)
    }
    
    @Test
    fun testUpdateTodo() = runBlocking {
        // Arrange
        val todo = Todo(
            id = 1,
            title = "Updated Todo",
            description = "Updated Description",
            isCompleted = true,
            priority = Todo.Priority.MEDIUM
        )
        
        // Act
        repository.update(todo)
        
        // Assert
        verify(todoDao).update(todo)
    }
    
    @Test
    fun testDeleteTodo() = runBlocking {
        // Arrange
        val todo = Todo(
            id = 1,
            title = "Test Todo",
            description = "Test Description"
        )
        
        // Act
        repository.delete(todo)
        
        // Assert
        verify(todoDao).delete(todo)
    }
    
    @Test
    fun testGetTodoById() {
        runBlocking {
            // Arrange
            val todoId = 1
            val expectedTodo = Todo(
                id = todoId,
                title = "Test Todo",
                description = "Test Description"
            )
            whenever(todoDao.getTodoById(todoId)).thenReturn(expectedTodo)
            
            // Act
            val result = repository.getTodoById(todoId)
            
            // Assert
            assert(result == expectedTodo)
            verify(todoDao).getTodoById(todoId)
        }
    }
    
    @Test
    fun testGetTodoByIdNotFound() {
        runBlocking {
            // Arrange
            val todoId = 999
            whenever(todoDao.getTodoById(todoId)).thenReturn(null)
            
            // Act
            val result = repository.getTodoById(todoId)
            
            // Assert
            assert(result == null)
            verify(todoDao).getTodoById(todoId)
        }
    }
    
    @Test
    fun testDeleteCompleted() = runBlocking {
        // Act
        repository.deleteCompleted()
        
        // Assert
        verify(todoDao).deleteCompleted()
    }
    
    @Test
    fun testSearchTodos() {
        // Arrange
        val searchQuery = "test"
        val mockLiveData = MutableLiveData<List<Todo>>()
        whenever(todoDao.searchTodos(searchQuery)).thenReturn(mockLiveData)
        
        // Act
        val result = repository.searchTodos(searchQuery)
        
        // Assert
        assert(result == mockLiveData)
        verify(todoDao).searchTodos(searchQuery)
    }
    
    @Test
    fun testAllTodosLiveData() {
        // Act
        val result = repository.allTodos
        
        // Assert - Just verify it returns a LiveData without crashing
        assert(result != null)
    }
}

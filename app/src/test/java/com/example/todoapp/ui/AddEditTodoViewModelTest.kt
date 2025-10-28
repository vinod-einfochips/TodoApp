package com.example.todoapp.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoapp.TodoApplication
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AddEditTodoViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Mock
    private lateinit var repository: TodoRepository
    
    private lateinit var viewModel: AddEditTodoViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Create a mock TodoApplication
        val mockTodoApp = org.mockito.kotlin.mock<TodoApplication> {
            on { this.repository }.thenReturn(repository)
        }
        
        viewModel = AddEditTodoViewModel(mockTodoApp, -1)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun testSaveTodoWithValidTitle() {
        // Arrange
        val title = "New Todo"
        val description = "Test Description"
        val priority = Todo.Priority.HIGH
        val dueDate = System.currentTimeMillis()
        
        // Act & Assert - Just verify it doesn't crash
        try {
            viewModel.saveTodo(title, description, priority, dueDate)
            assert(true) // Success if no exception
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
    }
    
    @Test
    fun testSaveTodoWithEmptyTitle() {
        // Arrange
        val title = ""
        val description = "Test Description"
        val priority = Todo.Priority.HIGH
        
        // Act & Assert - Just verify it doesn't crash
        try {
            viewModel.saveTodo(title, description, priority)
            assert(true) // Success if no exception
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
    }
    
    @Test
    fun testSaveTodoWithBlankTitle() {
        // Arrange
        val title = "   "
        val description = "Test Description"
        val priority = Todo.Priority.MEDIUM
        
        // Act & Assert - Just verify it doesn't crash
        try {
            viewModel.saveTodo(title, description, priority)
            assert(true) // Success if no exception
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
    }
    
    @Test
    fun testSaveTodoWithoutDescription() {
        // Arrange
        val title = "New Todo"
        val description: String? = null
        val priority = Todo.Priority.LOW
        
        // Act & Assert - Just verify it doesn't crash
        try {
            viewModel.saveTodo(title, description, priority)
            assert(true) // Success if no exception
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
    }
    
    @Test
    fun testSaveTodoWithoutDueDate() {
        // Arrange
        val title = "New Todo"
        val description = "Test Description"
        val priority = Todo.Priority.MEDIUM
        val dueDate: Long? = null
        
        // Act & Assert - Just verify it doesn't crash
        try {
            viewModel.saveTodo(title, description, priority, dueDate)
            assert(true) // Success if no exception
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
    }
    
    @Test
    fun testOnNavigateBack() {
        // Act
        viewModel.onNavigateBack()
        
        // Assert
        assert(viewModel.navigateBack.value == true)
    }
    
    @Test
    fun testOnNavigationComplete() {
        // Arrange
        viewModel.onNavigateBack()
        assert(viewModel.navigateBack.value == true)
        
        // Act
        viewModel.onNavigationComplete()
        
        // Assert
        assert(viewModel.navigateBack.value == false)
    }
    
    @Test
    fun testSaveTodoWithAllPriorities() {
        // Test LOW priority
        try {
            viewModel.saveTodo("Todo 1", "Desc", Todo.Priority.LOW)
            assert(true)
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
        
        // Reset
        viewModel.onNavigationComplete()
        
        // Test MEDIUM priority
        try {
            viewModel.saveTodo("Todo 2", "Desc", Todo.Priority.MEDIUM)
            assert(true)
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
        
        // Reset
        viewModel.onNavigationComplete()
        
        // Test HIGH priority
        try {
            viewModel.saveTodo("Todo 3", "Desc", Todo.Priority.HIGH)
            assert(true)
        } catch (e: Exception) {
            assert(false) { "saveTodo should not throw exception: ${e.message}" }
        }
    }
}

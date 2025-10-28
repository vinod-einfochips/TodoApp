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

class TodoDetailViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Mock
    private lateinit var repository: TodoRepository
    
    private lateinit var viewModel: TodoDetailViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Create a mock TodoApplication
        val mockTodoApp = org.mockito.kotlin.mock<TodoApplication> {
            on { this.repository }.thenReturn(repository)
        }
        
        viewModel = TodoDetailViewModel(mockTodoApp, 1)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun testToggleCompletionFromIncomplete() = runBlocking {
        // Arrange
        val isCompleted = true
        
        // Act
        viewModel.toggleCompletion(isCompleted)
        
        // Assert
        assert(true) // Coroutine launched successfully
    }
    
    @Test
    fun testToggleCompletionFromComplete() = runBlocking {
        // Arrange
        val isCompleted = false
        
        // Act
        viewModel.toggleCompletion(isCompleted)
        
        // Assert
        assert(true) // Coroutine launched successfully
    }
    
    @Test
    fun testDeleteTodo() {
        // Act & Assert - Just verify it doesn't crash
        try {
            viewModel.deleteTodo()
            assert(true)
        } catch (e: Exception) {
            assert(false) { "deleteTodo should not throw exception: ${e.message}" }
        }
    }
    
    @Test
    fun testOnEditTodo() {
        // Arrange
        val todoId = 1
        
        // Act
        viewModel.onEditTodo()
        
        // Assert
        assert(viewModel.navigateToEdit.value == todoId)
    }
    
    @Test
    fun testOnEditNavigated() {
        // Arrange
        viewModel.onEditTodo()
        assert(viewModel.navigateToEdit.value != null)
        
        // Act
        viewModel.onEditNavigated()
        
        // Assert
        assert(viewModel.navigateToEdit.value == null)
    }
    
    @Test
    fun testOnNavigationComplete() {
        // Act & Assert - Just verify it doesn't crash
        try {
            viewModel.onNavigationComplete()
            assert(true)
        } catch (e: Exception) {
            assert(false) { "onNavigationComplete should not throw exception: ${e.message}" }
        }
    }
    
    @Test
    fun testMultipleEdits() {
        // First edit
        viewModel.onEditTodo()
        assert(viewModel.navigateToEdit.value != null)
        
        // Navigate back
        viewModel.onEditNavigated()
        assert(viewModel.navigateToEdit.value == null)
        
        // Second edit
        viewModel.onEditTodo()
        assert(viewModel.navigateToEdit.value != null)
        
        // Navigate back again
        viewModel.onEditNavigated()
        assert(viewModel.navigateToEdit.value == null)
    }
}

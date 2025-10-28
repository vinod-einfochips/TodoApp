package com.example.todoapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var database: TodoDatabase
    private lateinit var todoDao: TodoDao
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        ).allowMainThreadQueries().build()
        
        todoDao = database.todoDao()
    }
    
    @After
    fun tearDown() {
        database.close()
    }
    
    @Test
    fun testInsertAndRetrieveTodo() = runBlocking {
        // Arrange
        val todo = Todo(
            title = "Test Todo",
            description = "Test Description",
            priority = Todo.Priority.HIGH
        )
        
        // Act
        todoDao.insert(todo)
        val retrievedTodo = todoDao.getTodoById(todo.id)
        
        // Assert
        assert(retrievedTodo != null)
        assert(retrievedTodo?.title == "Test Todo")
        assert(retrievedTodo?.description == "Test Description")
        assert(retrievedTodo?.priority == Todo.Priority.HIGH)
    }
    
    @Test
    fun testUpdateTodo() = runBlocking {
        // Arrange
        val originalTodo = Todo(
            title = "Original Title",
            description = "Original Description"
        )
        todoDao.insert(originalTodo)
        
        val updatedTodo = originalTodo.copy(
            title = "Updated Title",
            description = "Updated Description",
            isCompleted = true
        )
        
        // Act
        todoDao.update(updatedTodo)
        val retrievedTodo = todoDao.getTodoById(updatedTodo.id)
        
        // Assert
        assert(retrievedTodo?.title == "Updated Title")
        assert(retrievedTodo?.description == "Updated Description")
        assert(retrievedTodo?.isCompleted == true)
    }
    
    @Test
    fun testDeleteTodo() = runBlocking {
        // Arrange
        val todo = Todo(
            title = "Todo to Delete",
            description = "This will be deleted"
        )
        todoDao.insert(todo)
        
        // Act
        todoDao.delete(todo)
        val retrievedTodo = todoDao.getTodoById(todo.id)
        
        // Assert
        assert(retrievedTodo == null)
    }
    
    @Test
    fun testGetAllTodos() = runBlocking {
        // Arrange
        val todo1 = Todo(title = "Todo 1")
        val todo2 = Todo(title = "Todo 2")
        val todo3 = Todo(title = "Todo 3")
        
        todoDao.insert(todo1)
        todoDao.insert(todo2)
        todoDao.insert(todo3)
        
        // Act
        val allTodos = todoDao.getAllTodos()
        
        // Assert
        assert(allTodos.value != null)
        assert(allTodos.value?.size == 3)
    }
    
    @Test
    fun testSearchTodos() = runBlocking {
        // Arrange
        val todo1 = Todo(title = "Buy groceries", description = "Milk and eggs")
        val todo2 = Todo(title = "Complete project", description = "Android app")
        val todo3 = Todo(title = "Call dentist", description = "Appointment")
        
        todoDao.insert(todo1)
        todoDao.insert(todo2)
        todoDao.insert(todo3)
        
        // Act
        val searchResults = todoDao.searchTodos("project")
        
        // Assert
        assert(searchResults.value != null)
        assert(searchResults.value?.size == 1)
        assert(searchResults.value?.get(0)?.title == "Complete project")
    }
    
    @Test
    fun testSearchTodosByDescription() = runBlocking {
        // Arrange
        val todo1 = Todo(title = "Buy groceries", description = "Milk and eggs")
        val todo2 = Todo(title = "Complete project", description = "Android app")
        val todo3 = Todo(title = "Call dentist", description = "Appointment")
        
        todoDao.insert(todo1)
        todoDao.insert(todo2)
        todoDao.insert(todo3)
        
        // Act
        val searchResults = todoDao.searchTodos("Android")
        
        // Assert
        assert(searchResults.value != null)
        assert(searchResults.value?.size == 1)
        assert(searchResults.value?.get(0)?.description == "Android app")
    }
    
    @Test
    fun testDeleteCompleted() = runBlocking {
        // Arrange
        val completedTodo1 = Todo(title = "Completed 1", isCompleted = true)
        val completedTodo2 = Todo(title = "Completed 2", isCompleted = true)
        val incompleteTodo = Todo(title = "Incomplete", isCompleted = false)
        
        todoDao.insert(completedTodo1)
        todoDao.insert(completedTodo2)
        todoDao.insert(incompleteTodo)
        
        // Act
        todoDao.deleteCompleted()
        val remainingTodos = todoDao.getAllTodos()
        
        // Assert
        assert(remainingTodos.value != null)
        assert(remainingTodos.value?.size == 1)
        assert(remainingTodos.value?.get(0)?.title == "Incomplete")
    }
    
    @Test
    fun testTodoWithDueDate() = runBlocking {
        // Arrange
        val dueDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24) // Tomorrow
        val todo = Todo(
            title = "Todo with due date",
            description = "Has a due date",
            dueDate = dueDate
        )
        
        // Act
        todoDao.insert(todo)
        val retrievedTodo = todoDao.getTodoById(todo.id)
        
        // Assert
        assert(retrievedTodo?.dueDate == dueDate)
    }
    
    @Test
    fun testTodoWithPriority() = runBlocking {
        // Arrange
        val highPriorityTodo = Todo(
            title = "High Priority",
            priority = Todo.Priority.HIGH
        )
        val mediumPriorityTodo = Todo(
            title = "Medium Priority",
            priority = Todo.Priority.MEDIUM
        )
        val lowPriorityTodo = Todo(
            title = "Low Priority",
            priority = Todo.Priority.LOW
        )
        
        // Act
        todoDao.insert(highPriorityTodo)
        todoDao.insert(mediumPriorityTodo)
        todoDao.insert(lowPriorityTodo)
        
        val allTodos = todoDao.getAllTodos()
        
        // Assert
        assert(allTodos.value?.size == 3)
        assert(allTodos.value?.any { it.priority == Todo.Priority.HIGH } == true)
        assert(allTodos.value?.any { it.priority == Todo.Priority.MEDIUM } == true)
        assert(allTodos.value?.any { it.priority == Todo.Priority.LOW } == true)
    }
    
    @Test
    fun testGetNonExistentTodo() = runBlocking {
        // Act
        val retrievedTodo = todoDao.getTodoById(999)
        
        // Assert
        assert(retrievedTodo == null)
    }
}

package com.example.todoapp.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDatabaseTest {
    
    private lateinit var database: TodoDatabase
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        ).allowMainThreadQueries().build()
    }
    
    @After
    fun tearDown() {
        database.close()
    }
    
    @Test
    fun testDatabaseCreation() {
        // Assert
        assert(database.isOpen)
        assert(database.todoDao() != null)
    }
    
    @Test
    fun testDatabaseSingleton() {
        // Arrange
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        
        // Act
        val db1 = TodoDatabase.getDatabase(context)
        val db2 = TodoDatabase.getDatabase(context)
        
        // Assert
        assert(db1 === db2) // Same instance
    }
    
    @Test
    fun testMultipleTodoOperations() = runBlocking {
        // Arrange
        val dao = database.todoDao()
        val todos = listOf(
            Todo(title = "Todo 1", priority = Todo.Priority.HIGH),
            Todo(title = "Todo 2", priority = Todo.Priority.MEDIUM),
            Todo(title = "Todo 3", priority = Todo.Priority.LOW)
        )
        
        // Act
        todos.forEach { dao.insert(it) }
        val allTodos = dao.getAllTodos()
        
        // Assert
        assert(allTodos.value?.size == 3)
    }
    
    @Test
    fun testDatabasePersistence() = runBlocking {
        // Arrange
        val dao = database.todoDao()
        val todo = Todo(
            title = "Persistent Todo",
            description = "This should persist",
            priority = Todo.Priority.HIGH
        )
        
        // Act
        dao.insert(todo)
        val retrievedTodo = dao.getTodoById(todo.id)
        
        // Assert
        assert(retrievedTodo != null)
        assert(retrievedTodo?.title == "Persistent Todo")
    }
}

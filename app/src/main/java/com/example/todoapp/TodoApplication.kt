package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.data.TodoRepository

class TodoApplication : Application() {
    // Using by lazy so the database and repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { TodoDatabase.getDatabase(this) }
    val repository by lazy { TodoRepository(database.todoDao()) }
}

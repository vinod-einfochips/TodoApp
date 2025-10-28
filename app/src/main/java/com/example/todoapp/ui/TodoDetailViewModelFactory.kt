package com.example.todoapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TodoDetailViewModelFactory(
    private val application: Application,
    private val todoId: Int
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoDetailViewModel::class.java)) {
            return TodoDetailViewModel(application, todoId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

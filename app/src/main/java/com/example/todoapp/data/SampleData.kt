package com.example.todoapp.data

/**
 * Sample data for testing the app
 */
object SampleData {
    val sampleTodos = listOf(
        Todo(
            id = 1,
            title = "Buy groceries",
            description = "Milk, eggs, bread, and vegetables",
            isCompleted = false,
            priority = Todo.Priority.HIGH,
            dueDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24) // Tomorrow
        ),
        Todo(
            id = 2,
            title = "Complete project report",
            description = "Finish the quarterly report and send to manager",
            isCompleted = false,
            priority = Todo.Priority.HIGH,
            dueDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3) // 3 days from now
        ),
        Todo(
            id = 3,
            title = "Call dentist",
            description = "Schedule appointment for teeth cleaning",
            isCompleted = false,
            priority = Todo.Priority.MEDIUM,
            dueDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7) // 1 week from now
        ),
        Todo(
            id = 4,
            title = "Read Android documentation",
            description = "Study MVVM architecture and Room database",
            isCompleted = true,
            priority = Todo.Priority.LOW,
            dueDate = null
        ),
        Todo(
            id = 5,
            title = "Exercise",
            description = "Go for a 30-minute run",
            isCompleted = false,
            priority = Todo.Priority.MEDIUM,
            dueDate = System.currentTimeMillis() + (1000 * 60 * 60) // 1 hour from now
        ),
        Todo(
            id = 6,
            title = "Review code",
            description = "Review pull requests from team members",
            isCompleted = false,
            priority = Todo.Priority.HIGH,
            dueDate = System.currentTimeMillis() + (1000 * 60 * 60 * 2) // 2 hours from now
        ),
        Todo(
            id = 7,
            title = "Update resume",
            description = "Add recent projects and achievements",
            isCompleted = false,
            priority = Todo.Priority.LOW,
            dueDate = null
        ),
        Todo(
            id = 8,
            title = "Pay bills",
            description = "Electricity, water, and internet bills",
            isCompleted = true,
            priority = Todo.Priority.HIGH,
            dueDate = null
        )
    )
}

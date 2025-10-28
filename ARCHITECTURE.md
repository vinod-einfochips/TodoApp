# Todo App Architecture Guide

## Overview

This Todo app follows the **MVVM (Model-View-ViewModel)** architectural pattern combined with a **Repository pattern** for data access. This ensures clean separation of concerns, testability, and maintainability.

## Architecture Layers

### 1. **Presentation Layer (UI)**

The presentation layer consists of Android UI components that display data and handle user interactions.

#### Components:
- **Activities**: `MainActivity` - Entry point of the application
- **Fragments**: 
  - `TodoListFragment` - Displays list of todos
  - `AddEditTodoFragment` - Add or edit a todo
  - `TodoDetailFragment` - View todo details
- **Adapters**: `TodoListAdapter` - RecyclerView adapter for displaying todos
- **ViewModels**: Handle UI logic and state management

#### Responsibilities:
- Display data to the user
- Capture user interactions
- Navigate between screens
- Observe ViewModel data changes

### 2. **ViewModel Layer**

ViewModels manage UI-related data and handle communication between the UI and the repository.

#### ViewModels:

**TodoListViewModel**
```kotlin
- Manages the list of todos
- Handles navigation to add/edit screens
- Manages search functionality
- Handles todo deletion and completion toggle
```

**AddEditTodoViewModel**
```kotlin
- Manages todo creation and editing
- Validates input data
- Communicates with repository for save operations
- Handles navigation back to list
```

**TodoDetailViewModel**
```kotlin
- Loads and displays individual todo details
- Handles todo deletion
- Manages completion status toggle
- Handles navigation to edit screen
```

#### Key Features:
- Lifecycle-aware (survives configuration changes)
- Uses LiveData for reactive data binding
- Manages coroutines for async operations
- Observes repository data changes

### 3. **Repository Layer**

The repository acts as a single source of truth for data access, abstracting the data sources from the ViewModels.

#### TodoRepository
```kotlin
class TodoRepository(private val todoDao: TodoDao) {
    - Provides methods for CRUD operations
    - Exposes LiveData streams
    - Handles data transformation if needed
    - Manages database transactions
}
```

#### Responsibilities:
- Abstract data sources (local database, API, etc.)
- Provide a clean API for ViewModels
- Handle data consistency
- Manage database operations

### 4. **Data Layer**

The data layer handles all data persistence using Room database.

#### Components:

**Todo Entity**
```kotlin
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val dueDate: Long? = null,
    val priority: Priority = Priority.MEDIUM
)
```

**TodoDao (Data Access Object)**
```kotlin
@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllTodos(): LiveData<List<Todo>>
    
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)
    
    @Update
    suspend fun update(todo: Todo)
    
    @Delete
    suspend fun delete(todo: Todo)
    
    @Query("DELETE FROM todos WHERE isCompleted = 1")
    suspend fun deleteCompleted()
    
    @Query("SELECT * FROM todos WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    fun searchTodos(searchQuery: String): LiveData<List<Todo>>
}
```

**TodoDatabase**
```kotlin
@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    
    // Singleton pattern for database instance
}
```

## Data Flow

### Creating a Todo

```
User Input (AddEditTodoFragment)
    ↓
AddEditTodoViewModel.saveTodo()
    ↓
TodoRepository.insert()
    ↓
TodoDao.insert()
    ↓
Room Database
    ↓
LiveData emits new list
    ↓
TodoListViewModel observes change
    ↓
TodoListFragment updates UI
```

### Fetching Todos

```
TodoListFragment created
    ↓
TodoListViewModel initialized
    ↓
Repository.allTodos (LiveData)
    ↓
TodoDao.getAllTodos()
    ↓
Room Database query
    ↓
LiveData emits list
    ↓
Fragment observes and updates RecyclerView
```

### Updating a Todo

```
User edits todo (TodoDetailFragment)
    ↓
TodoDetailViewModel.toggleCompletion() or navigate to edit
    ↓
AddEditTodoViewModel.saveTodo()
    ↓
TodoRepository.update()
    ↓
TodoDao.update()
    ↓
Room Database
    ↓
LiveData emits updated list
    ↓
UI automatically updates
```

## Key Design Patterns

### 1. **MVVM Pattern**
- Separates UI logic from business logic
- ViewModels survive configuration changes
- LiveData provides reactive data binding

### 2. **Repository Pattern**
- Single source of truth for data
- Abstracts data sources
- Simplifies testing

### 3. **Singleton Pattern**
- TodoDatabase uses singleton pattern
- Ensures only one database instance exists

### 4. **Observer Pattern**
- LiveData implements observer pattern
- UI automatically updates when data changes

### 5. **Factory Pattern**
- ViewModelFactory creates ViewModels with dependencies
- Allows dependency injection

## Dependency Management

### External Dependencies
- **AndroidX**: Core Android libraries
- **Lifecycle**: ViewModel and LiveData
- **Room**: Local database
- **Navigation**: Fragment navigation
- **Material Design**: UI components
- **Coroutines**: Asynchronous programming

### Dependency Injection
Currently using manual dependency injection through:
- `TodoApplication` class for repository access
- `ViewModelFactory` for ViewModel creation

Future enhancement: Consider using Hilt for automatic dependency injection.

## Threading Model

### Main Thread
- UI updates
- Fragment/Activity operations
- LiveData observation

### Background Thread (Coroutines)
- Database operations (suspend functions in DAO)
- Data processing
- Repository operations

### Coroutine Scopes
- `viewModelScope`: Tied to ViewModel lifecycle
- Automatically cancelled when ViewModel is destroyed

## Error Handling

### Current Implementation
- Input validation in ViewModels
- Try-catch in repository operations
- Error messages displayed via UI

### Future Enhancements
- Comprehensive error handling strategy
- Error state in ViewModels
- User-friendly error messages

## Testing Strategy

### Unit Tests
- ViewModel tests (mock repository)
- Repository tests (mock DAO)
- Entity tests

### Integration Tests
- DAO tests with in-memory database
- Repository tests with real database

### UI Tests
- Fragment tests
- Navigation tests
- Adapter tests

## Performance Considerations

### Database Optimization
- Indexed queries for search
- Efficient sorting (isCompleted, createdAt)
- Pagination for large datasets (future)

### Memory Management
- LiveData lifecycle-aware
- ViewModel survives configuration changes
- Proper resource cleanup

### UI Performance
- DiffUtil for efficient RecyclerView updates
- ViewBinding for efficient view access
- Coroutines for non-blocking operations

## Security Considerations

### Data Protection
- Local database encryption (future enhancement)
- Input validation
- SQL injection prevention (Room handles this)

### User Privacy
- No external data transmission
- All data stored locally
- No analytics or tracking

## Future Architectural Improvements

1. **Dependency Injection**: Implement Hilt for automatic DI
2. **Error Handling**: Comprehensive error state management
3. **Pagination**: Handle large datasets efficiently
4. **Offline-First**: Sync with cloud when available
5. **Unit Testing**: Comprehensive test coverage
6. **Logging**: Structured logging for debugging
7. **Analytics**: Track user behavior (with consent)
8. **Encryption**: Encrypt sensitive data in database

## Conclusion

This architecture provides a solid foundation for a scalable, maintainable, and testable Android application. The separation of concerns, reactive data binding, and clean dependency management make it easy to add new features and maintain the codebase.

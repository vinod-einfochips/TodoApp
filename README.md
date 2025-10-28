# Todo App - Android Native

A modern Android Todo application built with **Kotlin**, **MVVM architecture**, and **Room database** for local data persistence.

## Features

✅ **Create, Read, Update, Delete (CRUD)** - Full todo management capabilities
✅ **Local Database** - All data stored locally using Room database
✅ **Priority Levels** - Organize todos by Low, Medium, and High priority
✅ **Due Dates** - Set and track due dates for todos
✅ **Search Functionality** - Search todos by title or description
✅ **Mark Complete** - Toggle todo completion status
✅ **Swipe to Delete** - Easy deletion with undo option
✅ **Material Design** - Modern UI with Material Design 3 components
✅ **MVVM Architecture** - Clean separation of concerns
✅ **LiveData** - Reactive data binding
✅ **Coroutines** - Asynchronous operations

## Architecture

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern:

```
┌─────────────────────────────────────────┐
│           UI Layer (Views)              │
│  (Fragments, Activities, Adapters)      │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      ViewModel Layer                    │
│  (TodoListViewModel,                    │
│   AddEditTodoViewModel,                 │
│   TodoDetailViewModel)                  │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer                   │
│  (TodoRepository)                       │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Data Layer                         │
│  (Room Database, DAO, Entities)         │
└─────────────────────────────────────────┘
```

## Project Structure

```
app/src/main/
├── java/com/example/todoapp/
│   ├── data/
│   │   ├── Todo.kt                 # Data entity
│   │   ├── TodoDao.kt              # Database access object
│   │   ├── TodoDatabase.kt         # Room database
│   │   └── TodoRepository.kt       # Data repository
│   └── ui/
│       ├── MainActivity.kt         # Main activity
│       ├── TodoListFragment.kt     # Todo list screen
│       ├── TodoListViewModel.kt    # Todo list ViewModel
│       ├── TodoListAdapter.kt      # RecyclerView adapter
│       ├── AddEditTodoFragment.kt  # Add/Edit todo screen
│       ├── AddEditTodoViewModel.kt # Add/Edit ViewModel
│       ├── TodoDetailFragment.kt   # Todo detail screen
│       └── TodoDetailViewModel.kt  # Detail ViewModel
├── res/
│   ├── layout/                     # XML layouts
│   ├── drawable/                   # Vector drawables
│   ├── values/                     # Strings, colors, dimens, styles
│   └── navigation/                 # Navigation graph
└── AndroidManifest.xml
```

## Key Technologies

- **Kotlin** - Modern Android development language
- **MVVM Architecture** - Clean architecture pattern
- **Room Database** - SQLite abstraction for local data persistence
- **LiveData** - Observable data holder
- **Coroutines** - Asynchronous programming
- **Navigation Component** - Fragment navigation
- **Material Design 3** - Modern UI components
- **ViewBinding** - Type-safe view access

## Dependencies

- `androidx.appcompat:appcompat` - AppCompat library
- `androidx.lifecycle:lifecycle-viewmodel-ktx` - ViewModel
- `androidx.lifecycle:lifecycle-livedata-ktx` - LiveData
- `androidx.room:room-runtime` - Room database
- `androidx.room:room-ktx` - Room coroutines support
- `androidx.navigation:navigation-fragment-ktx` - Navigation
- `com.google.android.material:material` - Material Design components
- `org.jetbrains.kotlinx:kotlinx-coroutines-android` - Coroutines

## Getting Started

### Prerequisites

- Android Studio (latest version)
- Android SDK 24 or higher
- Kotlin 1.8.0 or higher

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd TodoApp
```

2. Open the project in Android Studio

3. Build the project:
```bash
./gradlew build
```

4. Run on an emulator or device:
```bash
./gradlew installDebug
```

## Usage

### Creating a Todo
1. Tap the **+** button on the main screen
2. Enter the todo title (required)
3. Add an optional description
4. Set priority level (Low, Medium, High)
5. Select a due date (optional)
6. Tap **Save**

### Viewing Todo Details
1. Tap on any todo item in the list
2. View all details including title, description, priority, and due date
3. Mark as complete or delete from this screen

### Editing a Todo
1. Open the todo detail screen
2. Tap **Edit** button
3. Modify the details
4. Tap **Save**

### Deleting a Todo
1. **Swipe left or right** on a todo item to delete
2. Tap **Undo** in the snackbar to restore
3. Or open todo details and tap **Delete**

### Searching Todos
1. Use the search bar at the top of the list
2. Type to search by title or description
3. Tap the clear button to reset search

### Managing Completed Todos
1. Check the checkbox on any todo to mark as complete
2. Use the menu option **Delete Completed** to remove all completed todos

## Database Schema

### Todo Table
```sql
CREATE TABLE todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    isCompleted BOOLEAN DEFAULT 0,
    createdAt LONG NOT NULL,
    dueDate LONG,
    priority TEXT DEFAULT 'MEDIUM'
)
```

## Future Enhancements

- 📱 Notifications for due dates
- 🏷️ Categories/Tags for todos
- 📊 Statistics and analytics
- 🌙 Dark mode support
- 🔄 Sync with cloud storage
- 📅 Recurring todos
- 🔔 Reminders

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, email support@todoapp.com or open an issue in the repository.

---

**Happy Todo-ing! 🎯**

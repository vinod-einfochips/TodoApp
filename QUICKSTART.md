# Quick Start Guide - Todo App

Get up and running with the Todo app in 5 minutes!

## 1. Open in Android Studio

1. Launch Android Studio
2. Select **File** → **Open**
3. Navigate to the project folder
4. Click **Open**

Wait for Gradle sync to complete (you'll see a progress bar at the bottom).

## 2. Create an Emulator (if you don't have one)

1. Click **Tools** → **Device Manager**
2. Click **Create Device**
3. Select **Pixel 6** (or any device)
4. Select **API 34** (Android 14)
5. Click **Finish**

## 3. Run the App

1. Click the green **Run** button (▶) in the toolbar
2. Select your emulator
3. Click **OK**

The app will build and launch on your emulator!

## 4. Try the Features

### Create a Todo
- Tap the **+** button at the bottom right
- Enter a title (required)
- Add a description (optional)
- Select priority level
- Set a due date (optional)
- Tap **Save**

### View Todo Details
- Tap any todo in the list
- See all details
- Mark as complete or delete

### Edit a Todo
- Tap a todo to view details
- Tap **Edit**
- Modify the details
- Tap **Save**

### Delete a Todo
- Swipe left or right on a todo
- Tap **Undo** if you change your mind
- Or tap **Delete** in the detail view

### Search Todos
- Type in the search bar at the top
- Results update as you type
- Tap the X to clear search

## 5. Explore the Code

### Key Files to Review

**Data Layer**
- `app/src/main/java/com/example/todoapp/data/Todo.kt` - Data model
- `app/src/main/java/com/example/todoapp/data/TodoDao.kt` - Database queries
- `app/src/main/java/com/example/todoapp/data/TodoDatabase.kt` - Database setup

**UI Layer**
- `app/src/main/java/com/example/todoapp/ui/TodoListFragment.kt` - Todo list screen
- `app/src/main/java/com/example/todoapp/ui/AddEditTodoFragment.kt` - Add/edit screen
- `app/src/main/java/com/example/todoapp/ui/TodoDetailFragment.kt` - Detail screen

**ViewModel Layer**
- `app/src/main/java/com/example/todoapp/ui/TodoListViewModel.kt` - List logic
- `app/src/main/java/com/example/todoapp/ui/AddEditTodoViewModel.kt` - Add/edit logic
- `app/src/main/java/com/example/todoapp/ui/TodoDetailViewModel.kt` - Detail logic

## 6. Make Your First Change

### Change the App Name

1. Open `app/src/main/res/values/strings.xml`
2. Find the line: `<string name="app_name">Todo App</string>`
3. Change it to: `<string name="app_name">My Awesome Todo App</string>`
4. Press **Run** (the app will rebuild and restart)

### Change the Primary Color

1. Open `app/src/main/res/values/colors.xml`
2. Find: `<color name="colorPrimary">#6200EE</color>`
3. Change to any hex color, e.g., `#FF5722`
4. Press **Run**

## 7. Debug the App

### View Console Output
- Open **View** → **Tool Windows** → **Logcat**
- You'll see all app logs here

### Set a Breakpoint
1. Click on a line number in the code
2. A red dot will appear
3. Run the app in debug mode: **Run** → **Debug 'app'**
4. When that line is reached, execution pauses
5. Use the debug panel to step through code

### View Database Content
- Use Android Studio's **Database Inspector**
- **View** → **Tool Windows** → **Database Inspector**
- Browse the todo_database

## 8. Common Tasks

### Add a New Feature
1. Create a new ViewModel if needed
2. Create a new Fragment for UI
3. Add navigation in `nav_graph.xml`
4. Update the Repository if database changes needed

### Modify the Database
1. Edit `Todo.kt` entity
2. Update `TodoDao.kt` with new queries
3. Increment database version in `TodoDatabase.kt`
4. Update migration if needed

### Change UI Layout
1. Edit XML files in `app/src/main/res/layout/`
2. Use Android Studio's layout editor (visual or XML)
3. Press **Run** to see changes

## 9. Useful Keyboard Shortcuts

| Action | Windows/Linux | macOS |
|--------|---------------|-------|
| Run App | Shift+F10 | Ctrl+R |
| Debug App | Shift+F9 | Ctrl+D |
| Build Project | Ctrl+F9 | Cmd+F9 |
| Find | Ctrl+F | Cmd+F |
| Replace | Ctrl+H | Cmd+H |
| Format Code | Ctrl+Alt+L | Cmd+Alt+L |
| Rename | Shift+F6 | Shift+F6 |

## 10. Next Steps

- Read **README.md** for complete feature list
- Review **ARCHITECTURE.md** to understand the design
- Check **SETUP.md** for detailed configuration
- Explore the codebase and experiment!

## Troubleshooting

**App won't build?**
```bash
./gradlew clean build
```

**Emulator won't start?**
- Create a new AVD in Device Manager
- Or use a physical Android device

**Changes not showing up?**
- Clean and rebuild: `./gradlew clean build`
- Or in Android Studio: Build → Clean Project

**Database issues?**
- Clear app data: Settings → Apps → Todo App → Clear Data
- Restart the app

---

**You're all set! Happy coding! 🎉**

# Contributing to Todo App

Thank you for your interest in contributing to the Todo App! This document provides guidelines and instructions for contributing.

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Help others learn and grow
- Report issues responsibly

## Getting Started

### 1. Fork and Clone

```bash
# Fork the repository on GitHub
# Clone your fork
git clone https://github.com/your-username/todo-app.git
cd todo-app

# Add upstream remote
git remote add upstream https://github.com/original-repo/todo-app.git
```

### 2. Create a Branch

```bash
# Create a feature branch
git checkout -b feature/your-feature-name

# Or for bug fixes
git checkout -b fix/bug-description
```

### 3. Make Changes

Follow the coding standards and best practices outlined below.

### 4. Commit Changes

```bash
# Stage changes
git add .

# Commit with descriptive message
git commit -m "feat: add new feature description"
```

### 5. Push and Create Pull Request

```bash
# Push to your fork
git push origin feature/your-feature-name

# Create pull request on GitHub
```

## Coding Standards

### Kotlin Style Guide

Follow the [Kotlin official style guide](https://kotlinlang.org/docs/coding-conventions.html):

- **Indentation**: 4 spaces
- **Line length**: 120 characters
- **Naming**: camelCase for variables, PascalCase for classes
- **Imports**: Organize alphabetically

### Code Organization

```kotlin
// 1. Package declaration
package com.example.todoapp.ui

// 2. Imports (organized)
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

// 3. Class declaration
class TodoListFragment : Fragment() {
    
    // 4. Companion object (if needed)
    companion object {
        private const val TAG = "TodoListFragment"
    }
    
    // 5. Properties
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    
    // 6. Lifecycle methods
    override fun onCreateView(...): View { }
    override fun onViewCreated(...) { }
    override fun onDestroyView() { }
    
    // 7. Public methods
    fun publicMethod() { }
    
    // 8. Private methods
    private fun privateMethod() { }
}
```

### Naming Conventions

```kotlin
// Classes and interfaces
class TodoListFragment { }
interface TodoRepository { }

// Variables and functions
val todoList: List<Todo> = emptyList()
fun getTodoById(id: Int): Todo? { }

// Constants
companion object {
    private const val DATABASE_NAME = "todo_database"
}

// Private properties
private val _todos = MutableLiveData<List<Todo>>()
val todos: LiveData<List<Todo>> = _todos
```

### Documentation

Write clear comments and documentation:

```kotlin
/**
 * Loads a todo from the database by its ID.
 *
 * @param id The unique identifier of the todo
 * @return The todo if found, null otherwise
 */
suspend fun getTodoById(id: Int): Todo?

// Inline comments for complex logic
val sortedTodos = todos.sortedWith(
    compareBy<Todo> { it.isCompleted }  // Incomplete first
        .thenBy { it.createdAt }         // Then by creation date
)
```

## Architecture Guidelines

### MVVM Pattern

Maintain separation of concerns:

```
View (Fragment/Activity)
    ↓
ViewModel (Business Logic)
    ↓
Repository (Data Access)
    ↓
Data Layer (Database/API)
```

### Adding a New Feature

1. **Create Data Model** (if needed)
   ```kotlin
   @Entity(tableName = "table_name")
   data class NewEntity(...)
   ```

2. **Create DAO** (if needed)
   ```kotlin
   @Dao
   interface NewEntityDao { }
   ```

3. **Create ViewModel**
   ```kotlin
   class NewFeatureViewModel(application: Application) : AndroidViewModel(application) { }
   ```

4. **Create Fragment/Activity**
   ```kotlin
   class NewFeatureFragment : Fragment() { }
   ```

5. **Add Navigation** (if needed)
   ```xml
   <!-- In nav_graph.xml -->
   <fragment android:id="@+id/newFeatureFragment" ... />
   ```

## Testing

### Unit Tests

```kotlin
class TodoRepositoryTest {
    @Test
    fun testInsertTodo() {
        // Arrange
        val todo = Todo(title = "Test")
        
        // Act
        repository.insert(todo)
        
        // Assert
        assertEquals(todo, repository.getTodoById(todo.id))
    }
}
```

### Integration Tests

```kotlin
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
        ).build()
        todoDao = database.todoDao()
    }
}
```

## Commit Message Guidelines

Follow the [Conventional Commits](https://www.conventionalcommits.org/) format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that don't affect code meaning (formatting, etc.)
- **refactor**: Code change that neither fixes a bug nor adds a feature
- **perf**: Code change that improves performance
- **test**: Adding or updating tests
- **chore**: Changes to build process, dependencies, etc.

### Examples

```bash
git commit -m "feat(todo): add priority levels to todos"
git commit -m "fix(database): resolve migration issue"
git commit -m "docs: update README with new features"
git commit -m "refactor(viewmodel): simplify todo list logic"
```

## Pull Request Process

### Before Submitting

1. **Update your branch**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Run tests**
   ```bash
   ./gradlew test
   ```

3. **Check code quality**
   ```bash
   ./gradlew lint
   ```

4. **Build the project**
   ```bash
   ./gradlew build
   ```

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Related Issues
Closes #(issue number)

## Changes Made
- Change 1
- Change 2
- Change 3

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed

## Screenshots (if applicable)
Add screenshots for UI changes

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
- [ ] No new warnings generated
- [ ] Tests pass locally
```

## Review Process

1. **Code Review**: Maintainers will review your code
2. **Feedback**: Address any requested changes
3. **Approval**: PR is approved by maintainers
4. **Merge**: PR is merged into main branch

## Common Issues and Solutions

### Issue: Merge Conflicts

```bash
# Update your branch
git fetch upstream
git rebase upstream/main

# Resolve conflicts in your editor
# Then continue rebase
git add .
git rebase --continue
```

### Issue: Need to Update PR

```bash
# Make changes
git add .
git commit -m "fix: address review feedback"

# Force push to your branch
git push origin feature/your-feature-name --force
```

### Issue: Accidentally Committed to Main

```bash
# Create a new branch from current state
git branch feature/new-feature

# Reset main to upstream
git checkout main
git reset --hard upstream/main
```

## Documentation

### Update README

If your changes affect user-facing features, update README.md:

```markdown
## New Feature

Description of the new feature

### Usage
How to use the new feature
```

### Update ARCHITECTURE.md

If your changes affect architecture:

```markdown
## New Component

Description of the new component and how it fits into the architecture
```

## Performance Considerations

- Use `DiffUtil` for RecyclerView updates
- Avoid blocking operations on main thread
- Use `Coroutines` for async operations
- Optimize database queries with indexes
- Profile with Android Profiler

## Security Considerations

- Validate all user inputs
- Use Room's built-in SQL injection prevention
- Don't hardcode sensitive data
- Use secure storage for sensitive information
- Follow Android security best practices

## Resources

- [Android Developer Documentation](https://developer.android.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [MVVM Architecture Guide](https://developer.android.com/jetpack/guide)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Material Design](https://material.io/design)

## Questions?

- Check existing issues and discussions
- Open a new issue for questions
- Contact maintainers directly

## Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes
- Project documentation

Thank you for contributing! 🎉

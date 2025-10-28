package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

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

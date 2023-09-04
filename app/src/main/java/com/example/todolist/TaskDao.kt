package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Delete

@Dao
interface TaskDao {
    // ... other methods

    @Insert
fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<TaskEntity>>

    @Update
    fun updateTasks(tasks: List<TaskEntity>)


    @Delete
  fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
  fun deleteTaskById(taskId: Long)
}

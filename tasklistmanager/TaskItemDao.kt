package com.example.tasklistmanager

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskItemDao {
    @Query("SELECT * FROM task_item_table ORDER BY 'desc' DESC")
    fun allTaskItems(): Flow<List<TaskItem>>

    @Query("SELECT * FROM task_item_table WHERE isDone = false ORDER BY 'desc' DESC")
    fun unfinishedItems(): Flow<List<TaskItem>>

    @Query("SELECT * FROM task_item_table WHERE isDone = true ORDER BY 'desc' DESC")
    fun finishedItems(): Flow<List<TaskItem>>

    @Query("UPDATE task_item_table SET isDone = false, completedDateString = NULL WHERE dueHour =:hour")
    fun refreshCustom(hour: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem)

    @Update
    suspend fun updateTaskItem(taskItem: TaskItem)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)
}
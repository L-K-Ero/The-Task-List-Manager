package com.example.tasklistmanager

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskItemRepository(private val taskItemDao: TaskItemDao) {

    val unfinishedItems: Flow<List<TaskItem>> = taskItemDao.unfinishedItems()
    val allTaskItems: Flow<List<TaskItem>> = taskItemDao.allTaskItems()
    val finishedItems: Flow<List<TaskItem>> = taskItemDao.finishedItems()
    @WorkerThread
    fun refreshCustom(hour: Int) {
        taskItemDao.refreshCustom(hour)
    }
    @WorkerThread
    suspend fun insertTaskItem(taskItem: TaskItem) {
        taskItemDao.insertTaskItem(taskItem)
    }

    @WorkerThread
    suspend fun updateTaskItem(taskItem: TaskItem){
        taskItemDao.updateTaskItem(taskItem)
    }

    @WorkerThread
    suspend fun deleteTaskItem(taskItem: TaskItem){
        taskItemDao.deleteTaskItem(taskItem)
    }
}
package com.example.tasklistmanager

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(private val repository: TaskItemRepository): ViewModel() {
    var taskItems: LiveData<List<TaskItem>> = repository.unfinishedItems.asLiveData()
    var allItems: LiveData<List<TaskItem>> = repository.allTaskItems.asLiveData()
    var doneItems: LiveData<List<TaskItem>> = repository.finishedItems.asLiveData()


    fun addTaskItem(newTask: TaskItem) = viewModelScope.launch {
        repository.insertTaskItem(newTask)
    }

    fun updateTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.updateTaskItem(taskItem)
    }

    fun setCompleted(taskItem: TaskItem) = viewModelScope.launch {
        if(!taskItem.isCompleted())
            taskItem.completedDateString = TaskItem.dateFormatter.format(LocalDate.now())
        repository.updateTaskItem(taskItem)
    }

    fun deleteTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.deleteTaskItem(taskItem)
    }
}

class TaskItemModelFactory(private val repository: TaskItemRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java))
            return TaskViewModel(repository) as T

        throw IllegalArgumentException("Unknown Class for View Model")
    }
}
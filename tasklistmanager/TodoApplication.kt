package com.example.tasklistmanager

import android.app.Application
import androidx.work.Configuration

class TodoApplication: Application(), Configuration.Provider {
    private val database by lazy { TaskItemDatabase.getDatabase(this)}
    val repository by lazy { TaskItemRepository(database.taskItemDao())}

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
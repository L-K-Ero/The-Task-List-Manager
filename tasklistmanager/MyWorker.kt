package com.example.tasklistmanager

import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class MyWorker(context: Context,
               workerParameters: WorkerParameters,
               private val repository: TaskItemRepository)
    : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val dateObject = Date()
        val calendarInstance = Calendar.getInstance()
        calendarInstance.time = dateObject
        val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
        repository.refreshCustom(hour)
        Toast.makeText(applicationContext, "Worker Working", Toast.LENGTH_SHORT).show()
        // we do work here, since worker runs at the specified time, (current time), we can just use current time in refreshCustom().
        return Result.success()
    }

}
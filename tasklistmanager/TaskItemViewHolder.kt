package com.example.tasklistmanager

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklistmanager.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener

    ): RecyclerView.ViewHolder(binding.root){

        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        fun bindTaskItem(taskItem: TaskItem) {
            binding.name.text = taskItem.name

            if(taskItem.isCompleted()) {
                taskItem.isDone = true
                //binding.taskCellContainer.isInvisible = true
                //binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                //binding.dueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            binding.completeButton.setImageResource(taskItem.imageResource())
            binding.completeButton.setColorFilter(taskItem.imageColor(context))

            binding.completeButton.setOnClickListener{
                clickListener.completeTaskItem(taskItem)
                taskItem.isDone = true // we add another dialogue here to ask who completed task.
            }
            binding.taskCellContainer.setOnClickListener{

                    clickListener.editTaskItem(taskItem)

            }

            if(taskItem!!.dueTime() != null)
                binding.dueTime.text = timeFormat.format(taskItem.dueTime())
            else
                binding.dueTime.text = ""
        }

}
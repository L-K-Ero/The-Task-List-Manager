package com.example.tasklistmanager


import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tasklistmanager.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit


class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if(taskItem != null)
        {
            binding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.desc.text = editable.newEditable(taskItem!!.desc.toString()) // no idea what this wants
            if(taskItem!!.dueTime() != null){
                dueTime = taskItem!!.dueTime()!!
                updateTimeButtonText()
            }
        }
        else {
            binding.taskTitle.text = "New Task"
        }

        taskViewModel = ViewModelProvider(activity)[TaskViewModel::class.java]
        binding.saveButton.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
        binding.DeleteItem.setOnClickListener {
            deleteAction()
        }

    }

    private fun openTimePicker() {
        if(dueTime == null) {
            dueTime = LocalTime.now()
        }
        val listener = TimePickerDialog.OnTimeSetListener{_, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun deleteAction() {
        taskViewModel.deleteTaskItem(taskItem!!)
    }
    private fun saveAction() {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        val reqV = binding.checkBox.isChecked
        val mornings = binding.checkboxMornings.isChecked
        val midday = binding.checkBoxMid.isChecked
        val nights = binding.checkBoxNights.isChecked
        val dueTimeString = if(dueTime == null) {"12:00"} else TaskItem.timeFormatter.format(dueTime)

        if(dueTimeString == "12:00") {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 12)
            calendar.set(Calendar.MINUTE,0)
            calendar.set(Calendar.SECOND,0)
            val midnightTonight = calendar.timeInMillis

            val delay = midnightTonight - System.currentTimeMillis()

            val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()
            context?.let { WorkManager.getInstance(it).enqueue(workRequest) }
        } else {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, dueTime!!.hour)
            calendar.set(Calendar.MINUTE, dueTime!!.minute)
            calendar.set(Calendar.SECOND, 0)
            val midnightTonight = calendar.timeInMillis
            val delay = midnightTonight - System.currentTimeMillis()
            val constraints = Constraints.Builder()
                .build()
            val workRequest = PeriodicWorkRequestBuilder<MyWorker>(1,TimeUnit.DAYS,3,TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(delay,TimeUnit.MILLISECONDS)
                .build()
            context?.let { WorkManager.getInstance(it).enqueue(workRequest)}

        } // we set the time here to bring back tasks.

        if(taskItem == null) {
            val newTask = TaskItem(name, desc.toInt(), dueTimeString, 0,null, false, reqV)
            taskViewModel.addTaskItem(newTask)
        } else {
            taskItem!!.reqVerify = binding.checkBox.isChecked
            taskItem!!.name = name
            taskItem!!.desc = desc.toInt()
            taskItem!!.reqVerify = reqV
            taskItem!!.isDone = false
            taskItem!!.dueHour = dueTime!!.hour
            taskItem!!.dueTimeString = dueTimeString
            taskViewModel.updateTaskItem(taskItem!!)
        }
        binding.name.setText("")
        //binding.desc.value = 0
        dismiss()
    }


}
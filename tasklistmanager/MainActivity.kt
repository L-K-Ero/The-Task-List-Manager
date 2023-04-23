package com.example.tasklistmanager

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklistmanager.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), TaskItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context

    private val taskViewModel: TaskViewModel by viewModels {
        TaskItemModelFactory((application as TodoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adminPassword = "8473"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AdminButton.setOnClickListener {
            if(binding.adminPassword.visibility == View.GONE) {
                //remove admin button, show login and submit info
                binding.adminPassword.visibility = View.VISIBLE
                binding.SubmitButton.visibility = View.VISIBLE
                binding.AdminButton.visibility = View.GONE}
        }
        binding.SubmitButton.setOnClickListener {
            if(binding.newTaskButton.visibility == View.VISIBLE) {
                Toast.makeText(this@MainActivity, "Goodbye Admin", Toast.LENGTH_LONG).show()
                // Submit button will also return us out of admin permissions
                binding.AdminButton.visibility = View.VISIBLE
                binding.newTaskButton.visibility = View.GONE
                binding.SubmitButton.visibility = View.GONE
                binding.sendSMSFragment.visibility = View.GONE
                setRecyclerView()

            }
            else if(binding.adminPassword.text.toString() == adminPassword) {
                Toast.makeText(this@MainActivity,"Welcome Admin",Toast.LENGTH_LONG).show()
                // Submit password and clear password field, show admin stuff.
                setAllRecyclerView()
                binding.adminPassword.text.clear()
                binding.adminPassword.visibility = View.GONE
                binding.newTaskButton.visibility = View.VISIBLE
                binding.AdminButton.visibility = View.GONE
                binding.sendSMSFragment.visibility = View.VISIBLE

            } else {
                binding.adminPassword.text.clear()
                Toast.makeText(this@MainActivity,
                "Invalid Password",Toast.LENGTH_LONG).show()
                binding.AdminButton.visibility = View.VISIBLE
                binding.adminPassword.visibility = View.GONE
                binding.SubmitButton.visibility = View.GONE
                setRecyclerView()
            }


        }

        binding.sendSMSFragment.setOnClickListener {
                    SendSMSFragment().show(supportFragmentManager, "messageTag")
        }

        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager,"newTaskTag")
        }
        setRecyclerView()
    }

    private fun setAllRecyclerView() {
        val mainActivity = this
        taskViewModel.allItems.observe(this) {
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }

    private fun setRecyclerView() {
        val mainActivity = this
        taskViewModel.taskItems.observe(this) {
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
        taskViewModel.setCompleted(taskItem)
    }
}
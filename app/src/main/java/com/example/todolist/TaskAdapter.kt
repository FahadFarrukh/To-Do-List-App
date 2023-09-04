package com.example.todolist

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.TaskEntity
import com.example.todolist.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskAdapter(
    private val context: Context,
    private val taskDao: TaskDao
) : ListAdapter<TaskEntity, TaskAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        val taskNumber = position + 1
        holder.bind(task, taskNumber)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val number: TextView = itemView.findViewById(R.id.number)
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView3: TextView = itemView.findViewById(R.id.textView3)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(task: TaskEntity, taskNumber: Int) {
            number.text = "Task $taskNumber"
            textView1.text = task.name
            textView2.text = task.description
            textView3.text = task.date

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showDeleteConfirmationDialog(task)
                }
            }
        }

        private fun showDeleteConfirmationDialog(task: TaskEntity) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Task Completed")
            alertDialogBuilder.setMessage("Do you wish to remove this task?")
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val taskId = task.id
                deleteTask(taskId)
            }
            alertDialogBuilder.setNegativeButton("No") { _, _ ->
                checkBox.isChecked = false
            }
            alertDialogBuilder.create().show()
        }

        private fun deleteTask(taskId: Long) {
            CoroutineScope(Dispatchers.IO).launch {
                // Delete the task from the database
                taskDao.deleteTaskById(taskId)

                // Fetch all remaining tasks
                val remainingTasksLiveData = taskDao.getAllTasks()
                val remainingTasks = remainingTasksLiveData.value.orEmpty()

                // Update task numbers for each task
                val updatedTasks = remainingTasks.mapIndexed { index, taskEntity ->
                    taskEntity.copy(taskNumber = index + 1)
                }

                // Update the database with the modified tasks
                taskDao.updateTasks(updatedTasks)
            }
        }

    }

    fun updateData(newData: List<TaskEntity>) {
        submitList(newData)
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem == newItem
        }
    }
}

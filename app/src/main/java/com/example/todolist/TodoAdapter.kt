package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todoList: MutableList<TodoItem>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNameTextView: TextView = itemView.findViewById(R.id.taskNameTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item_layout, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = todoList[position]
        holder.taskNameTextView.text = todoItem.taskName

        // Add a click listener to the "Delete" button
        holder.deleteButton.setOnClickListener {
            // Remove the item from the list
            val removedItem = todoList.removeAt(holder.adapterPosition)
            // Notify the adapter about the item removal
            notifyItemRemoved(holder.adapterPosition)

            // You can also perform any additional actions here, like deleting the item from a data source.
            // For now, let's assume there is no data source to update.
        }
    }

    override fun getItemCount(): Int = todoList.size
}

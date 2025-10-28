package com.example.todoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Todo
import com.example.todoapp.databinding.ItemTodoBinding

class TodoListAdapter(
    private val onTodoClick: (Todo) -> Unit,
    private val onTodoCheckboxClick: (Todo, Boolean) -> Unit
) : ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        holder.bind(todo, onTodoClick, onTodoCheckboxClick)
    }

    class TodoViewHolder(
        private val binding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            todo: Todo,
            onTodoClick: (Todo) -> Unit,
            onTodoCheckboxClick: (Todo, Boolean) -> Unit
        ) {
            binding.apply {
                titleText.text = todo.title
                descriptionText.text = todo.description
                checkBox.isChecked = todo.isCompleted
                priorityIndicator.setBackgroundColor(
                    when (todo.priority) {
                        Todo.Priority.HIGH -> root.context.getColor(android.R.color.holo_red_light)
                        Todo.Priority.MEDIUM -> root.context.getColor(android.R.color.holo_orange_light)
                        Todo.Priority.LOW -> root.context.getColor(android.R.color.holo_green_light)
                    }
                )

                root.setOnClickListener { onTodoClick(todo) }
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onTodoCheckboxClick(todo, isChecked)
                }
            }
        }
    }
}

class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}

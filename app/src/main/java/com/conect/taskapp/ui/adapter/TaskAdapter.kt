package com.conect.taskapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.conect.taskapp.R
import com.conect.taskapp.data.Status
import com.conect.taskapp.data.Task
import com.conect.taskapp.databinding.ItemTaskBinding

class TaskAdapter(
    private val context: Context,
    private val taskSelected: (Task, Int) -> Unit
) : ListAdapter<Task, TaskAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val SELECT_BACK: Int = 1
        val SELECT_REMOVE: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_DETAILS: Int = 4
        val SELECT_NEXT: Int = 5

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(
                oldItem: Task,
                newItem: Task
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Task,
                newItem: Task
            ): Boolean {
                return when {
                    oldItem.id != newItem.id -> {
                        false
                    }

                    oldItem.status != newItem.status -> {
                        false
                    }

                    oldItem.description != newItem.description -> {
                        false
                    }

                    else -> true
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return (MyViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding.textDescription.text = task.description
        setIndicators(task, holder)

        holder.binding.btnRemover.setOnClickListener { taskSelected(task, SELECT_REMOVE) }
        holder.binding.btnEdit.setOnClickListener { taskSelected(task, SELECT_EDIT) }
        holder.binding.btnDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }
    }

    private fun setIndicators(task: Task, holder: MyViewHolder) {
        when (task.status) {
            Status.TODO -> {
                holder.binding.btnBack.isVisible = false

                holder.binding.btnNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }

            Status.DOING -> {
                holder.binding.btnBack.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.orange
                    )
                )

                holder.binding.btnNext.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.yellow
                    )
                )

                holder.binding.btnBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
                holder.binding.btnNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }

            Status.DONE -> {

                holder.binding.btnNext.isVisible = false
                holder.binding.btnBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
            }
        }
    }

    inner class MyViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)
}
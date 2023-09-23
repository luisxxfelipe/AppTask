package com.conect.taskapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.conect.taskapp.data.Task
import com.conect.taskapp.databinding.ItemTaskTopBinding

class TaskTopAdapter(
    private val taskTopSelected: (Task, Int) -> Unit
) : ListAdapter<Task, TaskTopAdapter.MyViewHolder>(DIFF_CALLBACK){

    companion object{
        val SELECT_REMOVE: Int = 1
        val SELECT_EDIT: Int = 2
        val SELECT_DETAILS: Int = 3

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(
                oldItem: Task,
                newItem: Task
            ):Boolean{
                return oldItem.id == newItem.id && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(
                oldItem: Task,
                newItem: Task
            ): Boolean{
                return oldItem == newItem && oldItem.description == newItem.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return (MyViewHolder(
            ItemTaskTopBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding.textDescription.text = task.description

        holder.binding.btnRemover.setOnClickListener{taskTopSelected(task, SELECT_REMOVE)}
        holder.binding.btnEdit.setOnClickListener{taskTopSelected(task, SELECT_EDIT)}
        holder.binding.btnDetails.setOnClickListener{taskTopSelected(task, SELECT_DETAILS)}
    }

inner class MyViewHolder(val binding: ItemTaskTopBinding) : RecyclerView.ViewHolder(binding.root)
}
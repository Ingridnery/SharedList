package br.edu.scl.ifsp.inery.sharedlist.adapter

import android.graphics.Paint
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.inery.sharedlist.databinding.TileTaskBinding
import br.edu.scl.ifsp.inery.sharedlist.model.Task

class TaskAdapter (
    private val taskList: MutableList<Task>,
    private val onTaskClickListenner: OnTaskClickListenner
    ): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
        inner class TaskViewHolder(tileTaskBinding: TileTaskBinding): RecyclerView.ViewHolder(tileTaskBinding.root), View.OnCreateContextMenuListener{
            val titleTv: TextView = tileTaskBinding.titleTv
            val dateTv: TextView = tileTaskBinding.dateTv
            var taskPosition= -1
            init{
                tileTaskBinding.root.setOnCreateContextMenuListener(this)
            }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
            menu?.add(Menu.NONE, 0,0, "Editar")?.setOnMenuItemClickListener {
                if (taskPosition != -1){
                    onTaskClickListenner.onEditMenuItemClick(taskPosition)
                }
                true
            }
            menu?.add(Menu.NONE, 1,1, "Remover")?.setOnMenuItemClickListener {
                if (taskPosition != -1){
                    onTaskClickListenner.onRemoveMenuItemClick(taskPosition)
                }
                true
            }
            menu?.add(Menu.NONE, 2,2, "Detalhar")?.setOnMenuItemClickListener {
                if (taskPosition != -1){
                    onTaskClickListenner.onDetailMenuItemClick(taskPosition)
                }
                true
            }
            menu?.add(Menu.NONE, 3,3, "Concluir")?.setOnMenuItemClickListener {
                if (taskPosition != -1){
                    onTaskClickListenner.onConcludeMenuItemClick(taskPosition)
                }
                true
            }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))
        val taskViewHolder = TaskViewHolder(tileTaskBinding)
        return taskViewHolder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        Log.v("aaaaaaaa", task.isCompleted.toString())
        if (task.isCompleted) {
            holder.titleTv.paintFlags = holder.titleTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.titleTv.paintFlags = holder.titleTv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        holder.titleTv.text = task.title
        holder.dateTv.text = task.datePreviousFinish.toString()
        holder.taskPosition = position

        holder.itemView.setOnClickListener {
            onTaskClickListenner.onTileTaskClick(position)
        }
    }
    override fun getItemCount(): Int = taskList.size

}
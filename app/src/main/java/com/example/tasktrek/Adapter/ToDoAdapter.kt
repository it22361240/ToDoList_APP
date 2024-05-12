package com.example.tasktrek.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktrek.AddNewTask
import com.example.tasktrek.MainActivity
import com.example.tasktrek.R
import com.example.tasktrek.Utils.DataBaseHelper
import com.example.tasktrek.model.ToDoModel

class ToDoAdapter(private val myDB: DataBaseHelper, private val activity: FragmentActivity) :
    RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var mList: List<ToDoModel> = ArrayList()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)

        init {
            mCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (isChecked) {
                    myDB.updateStatus(mList[position].id.toString(), 1)
                } else {
                    myDB.updateStatus(mList[position].id.toString(), 0)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.mCheckBox.text = item.task.toString()
        holder.mCheckBox.isChecked = item.status != 0
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setTasks(mList: List<ToDoModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int) {
        val item = mList[position]
        myDB.deleteTask(item.id.toString())
        val mutableList = mList.toMutableList()
        mutableList.removeAt(position)
        mList = mutableList.toList()
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = mList[position]

        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("task", item.task.toString())

        val task = AddNewTask()
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.tag)
    }
}

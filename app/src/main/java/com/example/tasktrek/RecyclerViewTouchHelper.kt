package com.example.tasktrek

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktrek.Adapter.ToDoAdapter

class RecyclerViewTouchHelper(private val adapter: ToDoAdapter, private val context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.RIGHT) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Task")
            builder.setMessage("Are You Sure ?")
            builder.setPositiveButton("Yes") { dialog, which -> adapter.deleteTask(position) }
            builder.setNegativeButton("Cancel") { dialog, which -> adapter.notifyItemChanged(position) }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }
}

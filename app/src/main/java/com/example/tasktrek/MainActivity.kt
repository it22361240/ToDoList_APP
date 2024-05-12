package com.example.tasktrek

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktrek.Adapter.ToDoAdapter
import com.example.tasktrek.Utils.DataBaseHelper
import com.example.tasktrek.model.ToDoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity(), OnDialogCloseListener {

    private lateinit var mRecyclerview: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var myDB: DataBaseHelper
    private var mList: MutableList<ToDoModel> = ArrayList()
    private lateinit var adapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)

        mRecyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        myDB = DataBaseHelper(this)
        adapter = ToDoAdapter(myDB, this)

        mRecyclerview.setHasFixedSize(true)
        mRecyclerview.layoutManager = LinearLayoutManager(this)
        mRecyclerview.adapter = adapter

        mList = myDB.getAllTasks().toMutableList()
        mList.reverse()
        adapter.setTasks(mList)

        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter, this))
        itemTouchHelper.attachToRecyclerView(mRecyclerview)
    }

    override fun onDialogClose(dialogInterface: DialogInterface) {
        mList = myDB.getAllTasks().toMutableList()
        mList.reverse()
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()
    }
}

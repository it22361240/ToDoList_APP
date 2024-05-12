package com.example.tasktrek.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tasktrek.model.ToDoModel

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    private val mDb: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME($COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, $COL_2 TEXT, $COL_3 INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(model: ToDoModel) {
        val values = ContentValues().apply {
            put(COL_2, model.task)
            put(COL_3, 0)
        }
        mDb.insert(TABLE_NAME, null, values)
    }

    fun updateTask(id: Int, task: String) {
        val values = ContentValues().apply {
            put(COL_2, task)
        }
        mDb.update(TABLE_NAME, values, "$COL_1=?", arrayOf(id.toString()))
    }

    fun updateStatus(id: String, status: Int) {
        val values = ContentValues().apply {
            put(COL_3, status)
        }
        mDb.update(TABLE_NAME, values, "$COL_1=?", arrayOf(id))
    }

    fun deleteTask(id: String) {
        mDb.delete(TABLE_NAME, "$COL_1=?", arrayOf(id))
    }

    @SuppressLint("Range")
    fun getAllTasks(): List<ToDoModel> {
        val cursor: Cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null)
        val taskList: MutableList<ToDoModel> = mutableListOf()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex(COL_1))
                val task = it.getString(it.getColumnIndex(COL_2))
                val status = it.getInt(it.getColumnIndex(COL_3))
                taskList.add(ToDoModel(id, task, status))
            }
        }
        return taskList
    }

    companion object {
        private const val DATABASE_NAME = "TODO_DATABASE"
        private const val TABLE_NAME = "TODO_TABLE"
        private const val COL_1 = "ID"
        private const val COL_2 = "TASK"
        private const val COL_3 = "STATUS"
    }
}
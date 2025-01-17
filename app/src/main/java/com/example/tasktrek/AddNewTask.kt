package com.example.tasktrek

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.tasktrek.model.ToDoModel
import com.example.tasktrek.Utils.DataBaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    //widgets
    private lateinit var mEditText: EditText
    private lateinit var mSaveButton: Button
    private lateinit var myDb: DataBaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.add_newtask, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEditText = view.findViewById(R.id.edittext)
        mSaveButton = view.findViewById(R.id.button_save)

        myDb = DataBaseHelper(requireActivity())

        var isUpdate = false

        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            mEditText.setText(task)

            if (task?.isNotEmpty() == true) {
                mSaveButton.isEnabled = false
            }

        }
        mEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    mSaveButton.isEnabled = true
                    mSaveButton.setBackgroundColor(Color.WHITE)
                } else {
                    mSaveButton.isEnabled = false
                    mSaveButton.setBackgroundColor(Color.GRAY)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        mSaveButton.setOnClickListener {
            val text = mEditText.text.toString()

            if (isUpdate) {
                val id = bundle?.getInt("id") ?: -1 // Provide a default value if the ID is null
                if (id != -1) { // Check if the ID is valid
                    myDb.updateTask(id, text)
                }

            } else {
                val item = ToDoModel(task = text) // Create ToDoModel with task text
                myDb.insertTask(item)
            }
            dismiss()
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is OnDialogCloseListener) {
            (activity as OnDialogCloseListener).onDialogClose(dialog)
        }
    }
}

package com.example.todoexample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.todoexample.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TaskBottomDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_task_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val checkButton = view.findViewById<Button>(R.id.checkButton)
        val editText: EditText = view.findViewById(R.id.editText)
        checkButton.setOnClickListener {
            val taskName: String = editText.text.toString()
            setFragmentResult("requestKey", bundleOf("bundleKey" to taskName))
            editText.text = null
            dismiss()
        }
    }
}
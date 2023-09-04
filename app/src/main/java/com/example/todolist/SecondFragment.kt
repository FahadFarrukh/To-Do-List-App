package com.example.todolist

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.todolist.Task
import com.example.todolist.databinding.FragmentSecondBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private var selectedDate: Long = 0 // Initialize selectedDate as a Long

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "user-database"
        )
            .fallbackToDestructiveMigration() // This will recreate the database on schema changes
            .build()


        binding.taskDateEditText.setOnClickListener {
            showDatePickerDialog()
        }


        binding.addButton.setOnClickListener {
            val name = binding.taskNameEditText.text.toString().trim()
            val description = binding.taskDescriptionEditText.text.toString().trim()
            val date = binding.taskDateEditText.text.toString().trim()


            if (name.isEmpty()) {
                val errorMessage = "Enter Task Name!"
                showErrorMessage(errorMessage)
            } else if (description.isEmpty()) {
                val errorMessage = "Enter Task Description!"
                showErrorMessage(errorMessage)
            }
            else if (date.isEmpty()) {
                val errorMessage = "Enter Task Date!"
                showErrorMessage(errorMessage)
            }
            else{


            val taskName = binding.taskNameEditText.text.toString()
            val taskDescription = binding.taskDescriptionEditText.text.toString()

            // Format the selected date
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(selectedDate)

            Thread {
                val user1 = TaskEntity(0, taskName, taskDescription, formattedDate)
                db.taskDao().insertTask(user1)

                val successMessage = "Task Added!"
                requireActivity().runOnUiThread {
                    showErrorMessage(successMessage)
                    nextpage()
                }
            }.start()
        }}
    }

    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Select a Date")
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            // Store the selected date as a Long timestamp
            selectedDate = selection
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(selection)

            // Convert the formattedDate String to an Editable
            val editableFormattedDate = Editable.Factory.getInstance().newEditable(formattedDate)

            // Set the Editable as the EditText's text
            binding.taskDateEditText.text = editableFormattedDate
        }

        picker.show(requireActivity().supportFragmentManager, picker.toString())
    }


    private fun showErrorMessage(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun nextpage() {
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

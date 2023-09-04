package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.example.todolist.databinding.FragmentFirstBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: TaskAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "user-database"
        )
            .fallbackToDestructiveMigration() // This will recreate the database on schema changes
            .build()


        val userListLiveData = db.taskDao().getAllTasks()

        userAdapter = TaskAdapter(requireContext(), db.taskDao()) // Pass the Context and TaskDao

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = userAdapter

        userListLiveData.observe(viewLifecycleOwner, Observer { userList ->
            if (userList.isEmpty()) {
                // No tasks available, show the empty state image
                binding.recyclerView.visibility = View.GONE
                binding.emptyStateImageView.visibility = View.VISIBLE
                binding.textView.visibility = View.VISIBLE
            } else {
                // Tasks are available, show the RecyclerView
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyStateImageView.visibility = View.GONE
                binding.textView.visibility = View.GONE

                // Update the adapter data by renumbering the tasks
                val updatedUserList = userList.mapIndexed { index, taskEntity ->
                    taskEntity.copy(taskNumber = index + 1)
                }
                userAdapter.updateData(updatedUserList)
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

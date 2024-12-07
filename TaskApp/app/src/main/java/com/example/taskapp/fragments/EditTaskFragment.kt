package com.example.taskapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentEditTaskBinding
import com.example.taskapp.model.Task
import com.example.taskapp.viewmodel.TaskViewModel


class EditTaskFragment : Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var tasksViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        tasksViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding.updateTaskTitle.setText(currentTask.taskTitle)
        binding.updateTaskDescription.setText(currentTask.description)
        binding.updateTaskDate.setText(currentTask.taskDate)
        binding.updateTaskPriority.setText(currentTask.priority)

        binding.UpdateFragmentButton.setOnClickListener{
            val taskTitle = binding.updateTaskTitle.text.toString().trim()
            val taskDescription = binding.updateTaskDescription.text.toString().trim()
            val taskDate = binding.updateTaskDate.text.toString().trim()
            val taskPriority = binding.updateTaskPriority.text.toString().trim()

            if(taskTitle.isNotEmpty()){
                val task = Task(currentTask.id, taskTitle,taskDescription,taskDate,taskPriority)
                tasksViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment,false)
                Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, "Please Enter Task Title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteTask(){
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Delete Task")
            setMessage("Do you want to Delete this Task?")
            setPositiveButton("Delete"){_,_ ->
                tasksViewModel.deleteTask(currentTask)
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
                Toast.makeText(context, "Task Deleted Successfully", Toast.LENGTH_SHORT).show()

            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteTask -> {
                deleteTask()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }


}
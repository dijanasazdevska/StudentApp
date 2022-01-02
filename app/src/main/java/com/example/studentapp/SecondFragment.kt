package com.example.studentapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.adapters.StudentRecyclerViewAdapter
import com.example.studentapp.databinding.FragmentSecondBinding
import com.example.studentapp.databinding.RecyclerViewRowBinding
import com.example.studentapp.models.Student
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), StudentRecyclerViewAdapter.OnStudentClickListener{

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val database = FirebaseDatabase.getInstance()
    private val studentsReference = database.getReference("students")
    private var students: MutableList<Student> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentRecyclerView: RecyclerView = view.findViewById(R.id.studentRecyclerView)
        val studentRecyclerViewAdapter = StudentRecyclerViewAdapter(students)
        studentRecyclerView.layoutManager = LinearLayoutManager(context)
        studentRecyclerView.adapter = studentRecyclerViewAdapter

        studentRecyclerViewAdapter.setOnClickListener(this)

        studentsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                students.clear()
                for (objSnapshot in snapshot.children) {
                    val student= objSnapshot .getValue(Student::class.java)
                    student?.key = objSnapshot.key!!
                    students.add(student!!)
                }
                studentRecyclerViewAdapter.updateData(students)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Database error", Toast.LENGTH_SHORT).show()
            }
        })




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun edit(position: Int) {
        val bundle = Bundle()

        bundle.putInt("position", position)
        bundle.putSerializable("student",students[position])
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
    }

    override fun delete(position: Int) {
        students.removeAt(position)
        studentsReference.setValue(students)
            .addOnCompleteListener(OnCompleteListener<Void?>{ task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Error: " + task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }


            })
    }
}
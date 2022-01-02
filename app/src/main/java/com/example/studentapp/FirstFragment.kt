package com.example.studentapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.studentapp.adapters.StudentRecyclerViewAdapter
import com.example.studentapp.databinding.FragmentFirstBinding
import com.example.studentapp.models.Student
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val database = FirebaseDatabase.getInstance()
    private val studentsReference = database.getReference("students")
    private lateinit var student: Student

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val indexEditText: EditText = view.findViewById(R.id.indexEditText)
        val nameEditText: EditText = view.findViewById(R.id.nameEditText)
        val surnameEditText: EditText = view.findViewById(R.id.surnameEditText)
        val phoneEditText: EditText = view.findViewById(R.id.phoneEditText)
        val addressEditText: EditText = view.findViewById(R.id.addressEditText)

        val bundle = arguments
        student = Student()
        if(bundle != null){
             student  = (bundle.getSerializable("student") as Student?)!!


            indexEditText.text.append(student.index)
            nameEditText.text.append(student.name)
            surnameEditText.text.append(student.surname)
            phoneEditText.text.append(student.phone)
            addressEditText.text.append(student.surname)
        }


        val saveButton: Button = view.findViewById(R.id.btnSaveStudent)

        saveButton.setOnClickListener(){
            val index = indexEditText.text.toString()
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val address = addressEditText.text.toString()

            if(index.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()){
                student.index = index
                student.name = name
                student.surname = surname
                student.phone = phone
                student.address = address

                if(student.key.isNotEmpty()){
                    val map = HashMap<String,Any>()
                    map.put(student.key,student)
                    studentsReference.updateChildren(map)
                        .addOnCompleteListener(OnCompleteListener<Void?>{ task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Error in update: " + task.exception!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })

                }
                else{

                    studentsReference.push().setValue(student)
                        .addOnCompleteListener(OnCompleteListener<Void?>{ task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

                            } else {
                                Toast.makeText(
                                    activity,
                                    "Error in saving: " + task.exception!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })

                }
            }
            else{
                Toast.makeText(activity,"Please fill in student informations!", Toast.LENGTH_LONG)
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
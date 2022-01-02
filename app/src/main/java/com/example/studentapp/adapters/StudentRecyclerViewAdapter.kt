package com.example.studentapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.R
import com.example.studentapp.models.Student

class StudentRecyclerViewAdapter(var students : MutableList<Student>) : RecyclerView.Adapter<StudentRecyclerViewAdapter.ViewHolder>(){

    private lateinit var onStudentClickListener: OnStudentClickListener
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val index: TextView
        val name: TextView
        val surname: TextView
        val phone: TextView
        val address: TextView
        val editButton: Button
        val deleteButton: Button

        init {
            index = view.findViewById(R.id.index)
            name = view.findViewById(R.id.name)
            surname = view.findViewById(R.id.surname)
            phone = view.findViewById(R.id.phone)
            address = view.findViewById(R.id.address)
            editButton = view.findViewById(R.id.editButton)
            deleteButton = view.findViewById(R.id.deleteButton)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return ViewHolder(view)
    }

    fun setOnClickListener(onStudentClickListener: OnStudentClickListener){
        this.onStudentClickListener = onStudentClickListener
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStudent = students[position]

        holder.index.text = "Index: ${currentStudent.index}"
        holder.name.text = "Name: ${currentStudent.name}"
        holder.surname.text = "Surname: ${currentStudent.surname}"
        holder.phone.text = "Phone: ${currentStudent.phone}"
        holder.address.text = "Address: ${currentStudent.address}"

        holder.editButton.setOnClickListener(){
            onStudentClickListener.edit(position)
        }

        holder.deleteButton.setOnClickListener(){
            onStudentClickListener.delete(position)
        }
        }

    override fun getItemCount(): Int {
        return students.size
    }

    fun updateData(students: MutableList<Student>){
        this.students = students
        notifyDataSetChanged()
    }

    interface  OnStudentClickListener {
        fun edit(position: Int)
        fun delete(position: Int)
    }
}
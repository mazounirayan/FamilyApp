
package com.example.familyapp.views.fragments.task

import UserRepository
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.familyapp.R
import com.example.familyapp.data.model.task.Priorite
import com.example.familyapp.data.model.task.StatusTache
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.task.TypeTache
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.dto.taskDto.TaskDto
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.viewmodel.factories.UserViewModelFactory
import java.time.LocalDate
import java.util.Calendar

class NewTaskFragment : Fragment() {

    private lateinit var usersSp: Spinner
    private lateinit var typeTaskSp: Spinner
    private lateinit var typePrioriteSp: Spinner
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView:TextView

    private var startDay: Int = 0
    private var startMonth: Int = 0
    private var startYear: Int = 0

    private var endDay: Int = 0
    private var endMonth: Int = 0
    private var endYear: Int = 0

    private lateinit var selectedUser: User
    private lateinit var typePriorite: Priorite
    private lateinit var typeTache: TypeTache

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(this.requireContext()),this)
    }

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(this.requireContext()),this)
    }

    @SuppressLint("SetTextI18n", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_task, container, false)

        view.findViewById<ImageView>(R.id.back_button_create_task).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        startDateTextView = view.findViewById(R.id.start_date_textview)
        endDateTextView = view.findViewById(R.id.end_date_textview)
        val startDateButton = view.findViewById<Button>(R.id.start_date_button)
        val endDateButton = view.findViewById<Button>(R.id.end_date_button)


        startDateButton.setOnClickListener {
            showDatePicker { day, month, year ->
                startDay = day
                startMonth = month
                startYear = year

                startDateTextView.text = "Date de début : $day/$month/$year"
            }
        }

        endDateButton.setOnClickListener {
            showDatePicker { day, month, year ->
                endDay = day
                endMonth = month
                endYear = year

                endDateTextView.text = "Date de fin : $day/$month/$year"
            }

        }
        view.findViewById<Button>(R.id.save_button).setOnClickListener{
            addTask()
        }

        userViewModel.fetchUser(SessionManager.currentUser!!.id)

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTypeTaskSp(view)
        setUpTypePrioriteSp(view)
        fetchData(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(){

        val nomTask = view?.findViewById<TextView>(R.id.task_name_form)
        val descriptionTask = view?.findViewById<TextView>(R.id.task_description_form)





        if(nomTask.toString() == "" || startYear == 0 || startMonth == 0 || startDay == 0 || endYear == 0 || endMonth == 0 || endDay == 0  || descriptionTask.toString() == "" ){
            Toast.makeText(
                requireContext(),
                "Veuillez remplir tous les champs obligatoires.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val dateDebut = LocalDate.of(startYear,startMonth,startDay)
        val dateFin = LocalDate.of(endYear,endMonth,endDay)

        if(dateDebut>dateFin){
            Toast.makeText(
                requireContext(),
                "La date de début ne peut pas être postérieure à la date de fin.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        /*Log.d("addTask", "Nom task : ${nomTask?.text.toString()}")
        Log.d("addTask", "description task : ${descriptionTask?.text.toString()}")
        Log.d("addTask", "typeTaskSp : $typeTache")
        Log.d("addTask", "typePrioriteSp : $typePriorite")
        Log.d("addTask", "usersSp : ${selectedUser.id}")
        Log.d("addTask", "dateDebut : ${LocalDate.of(startYear,startMonth,startDay)}")
        Log.d("addTask", "dateFin : ${LocalDate.of(endYear,endMonth,endDay)}")
        */

        taskViewModel.addTask(
            TaskDto(0,
                nom = nomTask?.text.toString(),
                dateDebut = dateDebut.toString() ,
                dateFin = dateFin.toString(),
                status = StatusTache.A_FAIRE.toString(),
                type = typeTache.toString(),
                description = descriptionTask?.text.toString() ,
                priorite = typePriorite,
                idUser = selectedUser.id,
                idFamille = 1)
        )


    }

    private fun showDatePicker(onDateSelected: (Int, Int, Int) -> Unit) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                onDateSelected(selectedDay, selectedMonth + 1, selectedYear)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun getUsers(data: List<User>): List<User> {
        return data
    }

    private fun setUpTypeTaskSp(fragmentView: View) {
        val typeTacheList = TypeTache.entries
        this.typeTaskSp = fragmentView.findViewById(R.id.typeTacheSpinner)

        val adapter = ArrayAdapter(
            fragmentView.context,
            android.R.layout.simple_spinner_item,
            typeTacheList.map { it.name.replace("_", " ").lowercase() }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.typeTaskSp.adapter = adapter

        this.typeTaskSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeTache = typeTacheList[position]
                Log.d("Spinner", "Type sélectionné : $typeTache")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setUpTypePrioriteSp(fragmentView: View) {
        val typePrioriteList = Priorite.entries
        this.typePrioriteSp = fragmentView.findViewById(R.id.typePrioriteSpinner)

        val adapter = ArrayAdapter(
            fragmentView.context,
            android.R.layout.simple_spinner_item,
            typePrioriteList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        this.typePrioriteSp.adapter = adapter


        this.typePrioriteSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typePriorite = typePrioriteList[position]
                Log.d("Spinner", "Type sélectionné : $typePriorite")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun setUpUsersSp(users: List<User>, fragmentView: View) {
        this.usersSp = fragmentView.findViewById(R.id.assignee_spinner)

        val adapter: ArrayAdapter<User> = ArrayAdapter(
            fragmentView.context,
            android.R.layout.simple_spinner_item,
            users
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        this.usersSp.adapter = adapter

        this.usersSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedUser = users[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun fetchData(fragmentView: View) {
        userViewModel.user.observe(viewLifecycleOwner) { data ->
            if (data.isEmpty()) {
                Log.e("NewTaskFragment", "La liste des utilisateurs est vide.")
            } else {
                Log.d("NewTaskFragment", "Utilisateurs récupérés : ${data.size}")
                setUpUsersSp(getUsers(data), fragmentView)
            }
        }
        //  userViewModel.fetchUser(1)
    }
}

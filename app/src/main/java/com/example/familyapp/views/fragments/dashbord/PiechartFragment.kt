
package com.example.familyapp.views.fragments.dashbord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.familyapp.R
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class PiechartFragment : Fragment() {

    private lateinit var pieChart: PieChart

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(this.requireContext()), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_dashboard_tsk_progress, container, false)

        pieChart = view.findViewById(R.id.pie_chart)

        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNotEmpty()) {
                updatePieChart(tasks)
            }
        }

        taskViewModel.fetchAllTasks(1) // Remplace 1 par l'ID de la famille

        return view
    }

    private fun updatePieChart(tasks: List<Task>) {
        val totalTasks = tasks.size.toFloat()

        val notStartedTasks = tasks.count { it.status == "Non commencé" }.toFloat()
        val inProgressTasks = tasks.count { it.status == "En cours" }.toFloat()
        val finishedTasks = tasks.count { it.status == "Fini" }.toFloat()

        val notStartedPercentage = if (totalTasks > 0) (notStartedTasks / totalTasks) * 100 else 0f
        val inProgressPercentage = if (totalTasks > 0) (inProgressTasks / totalTasks) * 100 else 0f
        val finishedPercentage = if (totalTasks > 0) (finishedTasks / totalTasks) * 100 else 0f

        val entries = listOf(
            PieEntry(notStartedPercentage, "Non commencé"),
            PieEntry(inProgressPercentage, "En cours"),
            PieEntry(finishedPercentage, "Fini")
        )

        val dataSet = PieDataSet(entries, "Tâches")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextSize = 14f

        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Tâches"
        pieChart.setEntryLabelTextSize(12f)
        pieChart.animateY(1000)
        pieChart.invalidate()
    }
}

package id.fauzancode.runningtrackerapp.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.adapters.RunAdapter
import id.fauzancode.runningtrackerapp.databinding.FragmentStatisticsBinding
import id.fauzancode.runningtrackerapp.ui.viewmodels.StatisticsViewModel
import id.fauzancode.runningtrackerapp.utils.CustomMarkerView
import id.fauzancode.runningtrackerapp.utils.SortType

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var runAdapter: RunAdapter
    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)
        setupRecyclerView()
        subscribeToObservers()
        setupBarChart()

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            when(viewModel.sortType) {
                SortType.DATE -> {
                    spFilter.setSelection(0)
                }
                SortType.RUNNING_TIME -> {
                    spFilter.setSelection(1)
                }
                SortType.DISTANCE -> {
                    spFilter.setSelection(2)
                }
                SortType.CALORIES_BURNED -> {
                    spFilter.setSelection(3)
                }
                SortType.AVG_SPEED -> {
                    spFilter.setSelection(4)
                }
            }

            spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position) {
                        0 -> viewModel.sortRuns(SortType.DATE)
                        1 -> viewModel.sortRuns(SortType.RUNNING_TIME)
                        2 -> viewModel.sortRuns(SortType.DISTANCE)
                        3 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
                        4 -> viewModel.sortRuns(SortType.AVG_SPEED)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setupRecyclerView() {
        runAdapter = RunAdapter()
        binding.rvRuns.apply {
            adapter = runAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun subscribeToObservers() {
        viewModel.runs.observe(viewLifecycleOwner) {
            it?.let {
                runAdapter.submitList(it)
            }
        }
        viewModel.runsSortedByDate.observe(viewLifecycleOwner) {
            it?.let {
                val allAvgSpeeds = it.indices.map { i -> BarEntry(i.toFloat() , it[i].avgSpeedInKMH) }
                val barDataSet = BarDataSet(allAvgSpeeds, "Avg speed over time").apply {
                    valueTextColor = Color.BLACK
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }

                binding.apply {
                    barChart.data = BarData(barDataSet)
                    barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                    barChart.invalidate()
                }
            }
        }
    }

    private fun setupBarChart() {
        binding.barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        binding.barChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        binding.barChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        binding.barChart.apply {
            description.text = "Avg speed over time"
            legend.isEnabled = false
        }
    }
}
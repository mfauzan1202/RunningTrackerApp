package id.fauzancode.runningtrackerapp.ui.statistics

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.adapters.RunAdapter
import id.fauzancode.runningtrackerapp.databinding.FragmentStatisticsBinding
import id.fauzancode.runningtrackerapp.ui.viewmodels.StatisticsViewModel
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
    }
}
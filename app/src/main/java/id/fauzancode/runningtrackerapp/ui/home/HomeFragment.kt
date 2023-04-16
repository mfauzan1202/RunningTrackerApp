package id.fauzancode.runningtrackerapp.ui.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.adapters.HomeAdapter
import id.fauzancode.runningtrackerapp.databinding.FragmentHomeBinding
import id.fauzancode.runningtrackerapp.db.Statistics
import id.fauzancode.runningtrackerapp.ui.viewmodels.HomeViewModel
import id.fauzancode.runningtrackerapp.utils.TrackingUtils
import java.lang.Math.round

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdapter

    //ini yang perlu km gunakan untuk request permission
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val deniedList: List<String> = result.filter {
            !it.value
        }.map {
            it.key
        }
        when {
            deniedList.isNotEmpty() -> {
                val map = deniedList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) {
                        "DENIED"
                    } else {
                        "EXPLAINED"
                    }
                }
                map["DENIED"]?.let {
                    Snackbar.make(
                        binding.root,
                        "You need to grant all permission to use this app",
                        Snackbar.LENGTH_LONG
                    ).setAction("OK") {
                        requestPermissions()
                    }.show()
                }
                map["EXPLAINED"]?.let {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity?.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }

            else -> {
                //All request are permitted
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        requestPermissions()
        setupRecyclerView()
        subscribeToObservers()

        binding.apply {
            btnStartRun.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTrackingFragment())
            }

            btnSeeAll.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStatisticsFragment())
            }

            btnProfile.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //this function is used to called request permission launcher
    private fun requestPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter()
        binding.rvProgress.apply {
            adapter = homeAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun subscribeToObservers() {
        val listStatistics = mutableListOf<Statistics>()
        viewModel.totalTimeRun.observe(viewLifecycleOwner) {
            it?.let {
                val totalTimeRun = TrackingUtils.getFormattedStopWatchTime(it)
                listStatistics.add(Statistics("Total Time", totalTimeRun))
            }
        }
        viewModel.totalDistance.observe(viewLifecycleOwner) {
            it?.let {
                val km = it / 1000f
                val totalDistance = Statistics("Total Distance", "${round(km * 10f) / 10f}")
                listStatistics.add(totalDistance)
            }
        }
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner) {
            it?.let {
                val avgSpeedString = "${round(it * 10f) / 10f}"
                val totalAvgSpeed = Statistics("Total Avg Speed", avgSpeedString)
                listStatistics.add(totalAvgSpeed)
            }
        }
        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner) {
            it?.let {
                val totalCaloriesBurned = Statistics("Total Calories Burned", "$it")
                listStatistics.add(totalCaloriesBurned)
            }
        }

        homeAdapter.submitList(listStatistics)
    }

}
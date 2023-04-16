package id.fauzancode.runningtrackerapp.ui.tracking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.databinding.FragmentTrackingBinding
import id.fauzancode.runningtrackerapp.db.Runs
import id.fauzancode.runningtrackerapp.services.Polyline
import id.fauzancode.runningtrackerapp.services.TrackingService
import id.fauzancode.runningtrackerapp.ui.viewmodels.TrackingViewModel
import id.fauzancode.runningtrackerapp.utils.Constants.ACTION_PAUSE
import id.fauzancode.runningtrackerapp.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import id.fauzancode.runningtrackerapp.utils.Constants.ACTION_STOP
import id.fauzancode.runningtrackerapp.utils.Constants.MAP_ZOOM
import id.fauzancode.runningtrackerapp.utils.Constants.POLYLINE_COLOR
import id.fauzancode.runningtrackerapp.utils.Constants.POLYLINE_WIDTH
import id.fauzancode.runningtrackerapp.utils.TrackingUtils
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking), OnMapReadyCallback {

    private val viewModel: TrackingViewModel by viewModels()
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var curTimeInMillis = 0L

    @set:Inject
    var weight = 80f
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTrackingBinding.bind(view)
        binding.apply {
            btnFinishRun.setOnClickListener {
                zoomToSeeWholeTrack()
                endRunAndSaveToDb()
            }

            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@TrackingFragment)
            addAllPolylines()
        }

        subscribeToObservers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.custom_map
            )
        )
        map = googleMap

        binding.btnRun.setOnClickListener {
            toggleRun()
        }

        binding.btnCancelRun.setOnClickListener {
            showCancelTrackingDialog()
        }
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        }

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner) {
            curTimeInMillis = it
            val formattedTime = TrackingUtils.getFormattedStopWatchTime(curTimeInMillis, true)
            binding.tvTimer.text = formattedTime
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        binding.apply {
            if (!isTracking) {
                binding.btnRun.setImageResource(R.drawable.ic_start)
                btnFinishRun.visibility = View.VISIBLE
                btnCancelRun.visibility = View.VISIBLE
            } else {
                btnRun.setImageResource(R.drawable.ic_pause)
                btnFinishRun.visibility = View.GONE
                btnCancelRun.visibility = View.GONE
            }
        }
    }

    private fun toggleRun() {
        isTracking = if (isTracking) {
            sendCommandToService(ACTION_PAUSE)
            false
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            true
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun showCancelTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                stopRun()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP)
        findNavController().popBackStack()
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = TrackingUtils.getLatLngBounds(pathPoints)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtils.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val run = Runs(
                bmp,
                dateTimeStamp,
                avgSpeed.toLong(),
                distanceInMeters,
                curTimeInMillis.toFloat(),
                caloriesBurned
            )
            viewModel.insertRun(run)
            //make snackbar
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()

            stopRun()
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(actionPauseService: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = actionPauseService
            requireContext().startService(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}
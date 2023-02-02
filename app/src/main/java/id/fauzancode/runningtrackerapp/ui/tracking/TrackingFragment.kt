package id.fauzancode.runningtrackerapp.ui.tracking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.databinding.FragmentTrackingBinding
import id.fauzancode.runningtrackerapp.services.TrackingService
import id.fauzancode.runningtrackerapp.services.TrackingService.Companion.ACTION_START
import id.fauzancode.runningtrackerapp.services.TrackingService.Companion.ACTION_STOP

class TrackingFragment : Fragment(R.layout.fragment_tracking), OnMapReadyCallback {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private var isTracking = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTrackingBinding.bind(view)
        binding.apply {
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@TrackingFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }
    }

    private fun toggleRun() {
        isTracking = if (isTracking) {
            binding.btnToggleRun.setImageResource(R.drawable.ic_start)
            sendCommandToService(ACTION_STOP)
            false
        } else {
            binding.btnToggleRun.setImageResource(R.drawable.ic_pause)
            sendCommandToService(ACTION_START)
            true
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
package id.fauzancode.runningtrackerapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.apply {
            btnStartRun.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTrackingFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

    //this function is used to called request permission launcher
    private fun requestPermissions() {
        requestPermissionLauncher.launch(
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            )
        )
    }

}
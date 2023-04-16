package id.fauzancode.runningtrackerapp.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.fauzancode.runningtrackerapp.R
import id.fauzancode.runningtrackerapp.databinding.FragmentProfileBinding
import id.fauzancode.runningtrackerapp.utils.Constants.KEY_FIRST_TIME_TOGGLE
import id.fauzancode.runningtrackerapp.utils.Constants.KEY_NAME
import id.fauzancode.runningtrackerapp.utils.Constants.KEY_WEIGHT
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile){

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        if (!isFirstAppOpen) {
            loadFieldsFromSharedPreferences()
        }

        binding.btnSave.setOnClickListener {
            val success = writePersonalDataToSharedPreferences()
            if (success) {
                findNavController().popBackStack()
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun writePersonalDataToSharedPreferences() : Boolean {
        val name = binding.profileName.text.toString()
        val weight = binding.profileWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()

        return true
    }

    private fun loadFieldsFromSharedPreferences() {
        val name = sharedPref.getString(KEY_NAME, "")
        val weight = sharedPref.getFloat(KEY_WEIGHT, 80f)

        binding.profileName.setText(name)
        binding.profileWeight.setText(weight.toString())
    }
}
package id.fauzancode.runningtrackerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.fauzancode.runningtrackerapp.db.Runs
import id.fauzancode.runningtrackerapp.repositories.MainRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    fun insertRun(run: Runs) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}
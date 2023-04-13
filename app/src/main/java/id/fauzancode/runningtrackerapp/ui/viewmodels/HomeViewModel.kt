package id.fauzancode.runningtrackerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.fauzancode.runningtrackerapp.repositories.MainRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {
}
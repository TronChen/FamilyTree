package com.tron.familytree.profile.editepisode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.Episode
import com.tron.familytree.network.LoadApiStatus
import com.tron.familytree.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditEpisodeViewModel(
    private val repository: FamilyTreeRepository
    , private val episode: Episode
): ViewModel() {

    private val _selectedProperty = MutableLiveData<Episode>()
    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<Episode>
        get() = _selectedProperty

    val editTitle = MutableLiveData<String>()
    val editLocation = MutableLiveData<String>()
    val editContent = MutableLiveData<String>()
    var editDate = MutableLiveData<String>()

    var longitude = MutableLiveData<Double>()
    var latitude = MutableLiveData<Double>()


    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    init {
        _selectedProperty.value = episode
        editDate.value = episode.time
        editTitle.value = episode.title
        editLocation.value = "請選擇位址"
        editContent.value = episode.content
    }


    fun setEpisode() : Episode{
        return Episode(
            user = UserManager.name!!,
            title = editTitle.value!!,
            time = editDate.value!!,
            content = editContent.value!!,
            location = editLocation.value!!,
            latitude = latitude.value,
            longitude = longitude.value
        )
    }

    fun addUserEpisode (episode: Episode){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.addUserEpisode(episode)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                }
                is AppResult.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is AppResult.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

}
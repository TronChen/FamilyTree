package com.tron.familytree.profile.edituser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import com.tron.familytree.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditUserViewModel(
    private val repository: FamilyTreeRepository, private val user: User)
    : ViewModel() {

    private val _selectedProperty = MutableLiveData<User>()
    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<User>
        get() = _selectedProperty

    var liveEpisodes = MutableLiveData<List<Episode>>()

    val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>>
        get() = _episodes

    val userName = MutableLiveData<String>()
    val userImage = MutableLiveData<String>()
    val userBirth = MutableLiveData<String>()
    val userDeath = MutableLiveData<String>()
    val userBirthLocation = MutableLiveData<String>()
    var userGender : String = ""

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
        _selectedProperty.value = user
        userName.value = user.name
        userImage.value = user.userImage
        userBirthLocation.value = user.birthLocation

        if (_selectedProperty.value!!.birth == null){
           userBirth.value = "請選擇日期"
        }else{
            userBirth.value = user.birth
        }

        if (_selectedProperty.value!!.deathDate == null){
            userDeath.value = "現在"
        }else{
            userDeath.value = user.deathDate
        }


        if (FamilyTreeApplication.INSTANCE.isLiveDataDesign()) {
            getLiveEpisode(_selectedProperty.value!!)
        } else {
            getEpisode(_selectedProperty.value!!)
        }
    }

    fun getEpisode(user: User) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getEpisode(user)

            _episodes.value = when (result) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is AppResult.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is AppResult.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    fun getLiveEpisode(user: User) {
        liveEpisodes = repository.getLiveEpisode(user)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }


    fun setUser() : User{
        return User(
            name = userName.value!!,
            birth = userBirth.value,
            id = selectedProperty.value!!.id,
            deathDate = userDeath.value,
            birthLocation = userBirthLocation.value,
            gender = userGender,
            userImage = UserManager.photo.toString()
        )
    }


    fun uploadImage(path: String) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            userImage.value = repository.uploadImage(path).toString()
            when (val result = repository.uploadImage(path)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    userImage.value = result.data
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


    fun updateMember (user: User){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.updateMember(user)) {
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
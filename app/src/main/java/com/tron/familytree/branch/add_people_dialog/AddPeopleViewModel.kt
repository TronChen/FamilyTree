package com.tron.familytree.branch.add_people_dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddPeopleViewModel(
    private val repository: FamilyTreeRepository
    , private val user: User
) : ViewModel() {

    private val _selectedProperty = MutableLiveData<User>()
    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<User>
        get() = _selectedProperty

    val _updateImg = MutableLiveData<String>()
    // The external LiveData for the SelectedProperty
    val updateImg: LiveData<String>
        get() = _updateImg

    val _imgUri = MutableLiveData<String>()
    // The external LiveData for the SelectedProperty
    val imgUri: LiveData<String>
        get() = _imgUri

    val _userImage = MutableLiveData<String>()
    // The external LiveData for the SelectedProperty
    val userImage: LiveData<String>
        get() = _userImage

    var birthDate : String = ""
    var deathDate : String = "Alive"
    var gender : String = ""

    var userEditName : String = ""
    val _userEditName = MutableLiveData<String>()

    var userBirthLocation = ""
    val _userBirthLocation = MutableLiveData<String>()



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

    /**
     * Call getArticlesResult() on init so we can display status immediately.
     */
    init {
        _selectedProperty.value = user
    }

    fun uploadImage(path: String) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            _userImage.value = repository.uploadImage(path).toString()
            when (val result = repository.uploadImage(path)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _userImage.value = result.data
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

    fun addMember(user: User){
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.addMember(user)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
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

    fun updateMemberFatherId(user: User, newMember : User){
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateMemberFatherId(user,newMember)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
//                    leave(true)
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

    fun updateMemberMotherId(user: User, newMember : User){
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.updateMemberMotherId(user,newMember)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
//                    leave(true)
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

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }



    fun setChild() : User{
            val user = User(
            name = userEditName,
            birth = birthDate,
            id = "",
            userImage = userImage.value,
            gender = gender,
            deathDate = deathDate,
            birthLocation = userBirthLocation,
                fatherId = selectedProperty.value?.fatherId,
                motherId = selectedProperty.value?.motherId
        )
        Log.e("Add user", user.toString())
        return user
    }

    fun setNewMember() : User{
        val user = User(
            name = userEditName,
            birth = birthDate,
            id = "",
            userImage = userImage.value,
            gender = gender,
            deathDate = deathDate,
            birthLocation = userBirthLocation
        )
        Log.e("Add user", user.toString())
        return user
    }
}
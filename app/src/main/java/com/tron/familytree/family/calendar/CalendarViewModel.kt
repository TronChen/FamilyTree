package com.tron.familytree.family.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.Event
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import com.tron.familytree.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewModel(
    private val repository: FamilyTreeRepository
) : ViewModel() {

    //Selected date for safe arg
    private val _navigationToPostDialog = MutableLiveData<Long>()

    val navigationToPostDialog : LiveData<Long>
        get() = _navigationToPostDialog

    var allLiveEvents = MutableLiveData<List<Event>>()

    //Query Selected Events

    private var _selectedEvents = MutableLiveData<List<Event>>()

    val selectedEvent : LiveData<List<Event>>
        get() = _selectedEvents

    private var _user = MutableLiveData<User>()

    val user : LiveData<User>
        get() = _user

    val selectedLiveEvent = MutableLiveData<List<Event>>()


    var liveUserEvent = MutableLiveData<List<Event>>()

    var selectedUserEvent = MutableLiveData<List<Event>>()

    val _userEvent = MutableLiveData<List<Event>>()
    val userEvent: LiveData<List<Event>>
        get() = _userEvent

    val _selectedEvent = MutableLiveData<Event>()


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
        getLiveEventByUserId(UserManager.email.toString())

        findUserById(UserManager.email.toString())

        getEventByTime(SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().timeInMillis))
    }

    fun findUserById(id : String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.findUserById(id)

            when (result) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _user.value = result.data
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


    fun getEventByUserId(user: User) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getEventByUserId(user)

            _userEvent.value = when (result) {
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

    fun getEventByTime(date : String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getEventByTime(date)

            selectedUserEvent.value = when (result) {
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

    fun getLiveEventByUserId(id : String) {
        liveUserEvent = repository.getLiveEventByUserId(id)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }
}
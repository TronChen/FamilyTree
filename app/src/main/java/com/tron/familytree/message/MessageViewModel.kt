package com.tron.familytree.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import com.tron.familytree.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MessageViewModel(
    private val repository: FamilyTreeRepository, private val user: User
) : ViewModel() {

    private val _selectedProperty = MutableLiveData<User>()
    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<User>
        get() = _selectedProperty



    var liveChatroom = MutableLiveData<List<ChatRoom>>()

    val _chatroom = MutableLiveData<List<ChatRoom>>()
    val chatroom: LiveData<List<ChatRoom>>
        get() = _chatroom

    val _checkChatroom = MutableLiveData<List<ChatRoom>>()
    val checkChatroom: LiveData<List<ChatRoom>>
        get() = _checkChatroom


    val _chatMember = MutableLiveData<User>()
    val chatMember: LiveData<User>
        get() = _chatMember

    val _mySelf = MutableLiveData<User>()
    val mySelf: LiveData<User>
        get() = _mySelf

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

        if (FamilyTreeApplication.INSTANCE.isLiveDataDesign()) {
            getLiveChatroom()
        } else {
            getChatroom()
        }

        findUserMyself(UserManager.email.toString())
    }

    fun getChatroom() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getChatroom()

            _chatroom.value = when (result) {
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

    fun getLiveChatroom() {
        liveChatroom = repository.getLiveChatroom()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }


    fun addChatroom(chatRoom: ChatRoom){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.addChatroom(chatRoom)) {
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

    fun findUserById(id: String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUserById(id)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _chatMember.value = result.data
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

    fun findUserMyself(id: String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUserById(id)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _mySelf.value = result.data
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

    fun findChatroom(member: String, userId : String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findChatroom(member,userId)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    addChatroom(setChatroom(_chatMember.value!!))

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

    fun setChatroom(user: User) : ChatRoom{
        return ChatRoom(
            id = "",
            attenderId = listOf(user.id,UserManager.email.toString()),
            userImage = listOf(user.userImage.toString(),UserManager.photo.toString()),
            attenderName = listOf(_chatMember.value!!.name, UserManager.name.toString())
        )
    }
}
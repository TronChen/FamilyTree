package com.tron.familytree.profile.member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import com.tron.familytree.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MemberViewModel(
    private val repository: FamilyTreeRepository
): ViewModel() {

    val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    val _father = MutableLiveData<User>()
    val father : LiveData<User>
        get() = _father

    val _mother = MutableLiveData<User>()
    val mother : LiveData<User>
        get() = _mother

    val _mate = MutableLiveData<User>()
    val mate : LiveData<User>
        get() = _mate

    val _children = MutableLiveData<User>()
    val children : LiveData<User>
        get() = _children

    var userMemberList = mutableListOf<MemberItem>()
    val adapterList = MutableLiveData<List<MemberItem>>()


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
        UserManager.email?.let { findUserById(it) }
    }


    fun findUserById(id : String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUser(id)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _user.value = result.data

                    if (_user.value!!.fatherId != null) {
                        findFather(_user.value!!.fatherId!!)
                    }
                    if (_user.value!!.fatherId == null) {
                        userMemberList.add(
                            MemberItem.FatherAdd(
                                User(name = "No father", fatherId = _user.value!!.id)
                                , true, 0
                            )
                        )
                        if (_user.value!!.motherId != null) {
                            findMother(_user.value!!.motherId!!)
                        }
                        if (_user.value!!.motherId == null) {
                            userMemberList.add(
                            MemberItem.MotherAdd(
                                User(name = "No mother", motherId = _user.value!!.id)
                                , true, 1
                            )
                            )
                            if (_user.value!!.mateId != null){
                                findMate(_user.value!!.mateId!!)
                            }
                            if (_user.value!!.mateId == null){
                                userMemberList.add(
                                MemberItem.MateAdd(
                                    User(name = "No mate",mateId = _user.value!!.id),false
                                )
                                )
                                getMemberList()
                            }
                        }
                    }

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

    fun findFather(id : String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUser(id)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _father.value = result.data

                    if (_user.value!!.motherId != null) {

                    userMemberList.add(MemberItem.Father(
                        _father.value!!,true,0))

                        findMother(_user.value!!.motherId!!)
                    }
                    if (_user.value!!.motherId == null) {

                        userMemberList.add(MemberItem.MotherAdd(
                            User(name = "No mother",motherId = _user.value!!.id)
                            ,true,0))

                        if (_user.value!!.mateId != null) {
                            findMate(_user.value!!.mateId!!)
                        }
                        if (_user.value!!.mateId == null) {
                            userMemberList.add(MemberItem.MateAdd(
                                User(name = "No mate",mateId = _user.value!!.id)
                                ,true))
                            getMemberList()
                        }
                    }

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

    fun findMother(id : String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUser(id)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _mother.value = result.data

                        userMemberList.add(
                            MemberItem.Mother(
                                _mother.value!!,true,1))
                    if (_user.value!!.mateId != null) {
                        findMate(_user.value!!.mateId!!)
                    }
                    if (_user.value!!.mateId == null) {
                        userMemberList.add(MemberItem.MateAdd(
                            User(name = "No mate",mateId = _user.value!!.id)
                            ,true))
                        getMemberList()
                    }
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

    fun findMate(id : String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUser(id)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _mate.value = result.data

                    userMemberList.add(MemberItem.Mate(_mate.value!!,false))
                    findChildren(user.value!!)
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

    fun findChildren(user: User) {
        coroutineScope.launch {

            if (user.gender == "male") {
                FirebaseFirestore.getInstance().collection("User")
                    .whereEqualTo("fatherId", user.name)
                    .get()
                    .addOnSuccessListener { result ->
                        for ((index, document) in result.withIndex()) {
                            val child = document.toObject(User::class.java)
                            userMemberList.add(
                                MemberItem.Children(child, index)
                            )
                            getMemberList()
                        }
                        if (result.isEmpty) {
                            userMemberList.add(
                                MemberItem.ChildrenAdd(
                                    User(
                                        name = "No child",
                                        fatherId = user.name,
                                        motherId = user.mateId
                                    ), 0
                                )
                            )
                            getMemberList()
                        }

                    }
            }
            if (user.gender == "female") {
                FirebaseFirestore.getInstance().collection("User")
                    .whereEqualTo("motherId", user.name)
                    .get()
                    .addOnSuccessListener { result ->
                        for ((index, document) in result.withIndex()) {
                            val child = document.toObject(User::class.java)
                            userMemberList.add(
                                MemberItem.Children(child, index)
                            )
                            getMemberList()
                        }
                        if (result.isEmpty) {
                            userMemberList.add(
                                MemberItem.ChildrenAdd(
                                    User(
                                        name = "No child",
                                        fatherId = user.mateId,
                                        motherId = user.name
                                    ), 0
                                )
                            )
                            getMemberList()
                        }

                    }
            }
        }
    }

    fun getMemberList() {
            adapterList.value = userMemberList
    }
}
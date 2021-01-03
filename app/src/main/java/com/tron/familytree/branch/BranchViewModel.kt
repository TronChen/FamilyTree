package com.tron.familytree.branch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import com.tron.familytree.util.ServiceLocator.repository
import com.tron.familytree.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BranchViewModel(
    private val repository: FamilyTreeRepository
) : ViewModel() {

    var user = MutableLiveData<User>()
    val userId = MutableLiveData<String>()

    var mateId = MutableLiveData<User>()
    var fatherId = MutableLiveData<User>()
    var motherId = MutableLiveData<User>()
    var mateFatherId = MutableLiveData<User>()
    var mateMotherId = MutableLiveData<User>()

    var itemClick = MutableLiveData<Int>()
    var itemSelected = MutableLiveData<User>()

    val TreeList = MutableLiveData<List<TreeItem>>()

    var children = MutableLiveData<Int>()
    val onlyMate = mutableListOf<TreeItem>()

    var treeFinalList = mutableListOf<TreeItem>()

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
        userId.value = UserManager.email
        searchBranchUser(userId.value!!)
//        searchBranchUserChildren(userId.value!!)
    }

    fun searchBranchUser(id : String){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.searchBranchUser(id,this@BranchViewModel)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    TreeList.value = result.data
                    Log.e("TreeList",result.data.toString())
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


//    fun searchBranchUserChildren(id: String){
//
//        coroutineScope.launch {
//
//            _status.value = LoadApiStatus.LOADING
//
//            when (val result = repository.searchBranchUserChildren(id)) {
//                is AppResult.Success -> {
//                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
//                    children.value = result.data
//                    Log.e("TreeList",result.data.toString())
//                }
//                is AppResult.Fail -> {
//                    _error.value = result.error
//                    _status.value = LoadApiStatus.ERROR
//                }
//                is AppResult.Error -> {
//                    _error.value = result.exception.toString()
//                    _status.value = LoadApiStatus.ERROR
//                }
//                else -> {
//                    _error.value = FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)
//                    _status.value = LoadApiStatus.ERROR
//                }
//            }
//        }
//    }


    fun getSpanCount(size: Int): Int{
        return if (size > 4){
            size + 1
        } else{
            5
        }
    }


    fun reQuery(){
        treeFinalList.clear()
        onlyMate.clear()
        mateId.value = null
        fatherId.value = null
        motherId.value = null
        mateFatherId.value = null
        mateMotherId.value = null
        itemClick.value = null
    }

}
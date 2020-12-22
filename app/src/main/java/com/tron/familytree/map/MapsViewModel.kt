package com.tron.familytree.map

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.GlideCircleBorderTransform
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.Episode
import com.tron.familytree.data.Map
import com.tron.familytree.data.User
import com.tron.familytree.network.LoadApiStatus
import com.tron.familytree.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: FamilyTreeRepository
) : ViewModel() {

    val userImage = MutableLiveData<String>()

    val _userMarkerList = MutableLiveData<List<Marker>>()

    val _episodeMarkerList = MutableLiveData<List<Marker>>()

    val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    val _userTag = MutableLiveData<User>()
    val userTag: LiveData<User>
        get() = _userTag

    var liveUserLocation = MutableLiveData<List<Map>>()

    val _userLocation = MutableLiveData<List<Map>>()
    val userLocation : LiveData<List<Map>>
    get() = _userLocation

    val _episode = MutableLiveData<List<Episode>>()
    val episode : LiveData<List<Episode>>
        get() = _episode

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
        userImage.value = UserManager.photo.toString()

        findUserById(UserManager.email.toString())


        getUserLocation()
        getLiveUserLocation()
        getAllEpisode()

    }


    fun findUserById(id : String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUserById(id)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _user.value = result.data
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

    fun findUserByIdTag(tag : String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.findUserById(tag)) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _userTag.value = result.data
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

    fun getAllEpisode() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getAllEpisode()

            _episode.value = when (result) {
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

    fun getUserLocation() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getUserLocation()

            _userLocation.value = when (result) {
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

    fun getLiveUserLocation() {
        liveUserLocation = repository.getLiveUserLocation()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }


    fun drawUsersLocation(map: GoogleMap, usersLocationList: List<Map>) {

        val markerList = mutableListOf<Marker>()

        if (usersLocationList.isNotEmpty()) {
            usersLocationList.forEach {myLocation ->

                Glide.with(FamilyTreeApplication.INSTANCE.applicationContext)
                    .asBitmap()
                    .load(myLocation.userImage)
                    .apply(
//                    RequestOptions().transform(CenterCrop(), RoundedCorners(50), GlideCircleBorderTransform(100f, 10))
                        RequestOptions().transform(CenterCrop(), GlideCircleBorderTransform(135f, 0))
//                        RequestOptions().transform(GlideCircleBorderTransform(135f, 0))
                    )
                    .into( object : SimpleTarget<Bitmap>(150, 150) {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                        ) {
                            map?.apply {

                                val marker = addMarker(
                                    MarkerOptions()
                                        .title(myLocation.userId)
                                        .position(LatLng(myLocation.latitude!!, myLocation.longitude!!))
                                        .icon(BitmapDescriptorFactory.fromBitmap(resource)))
                                marker.tag = myLocation.userId
                                markerList.add(marker)
                                Log.e("MarkerTag", marker.tag as String)

                            }
                        }
                    })
            }
            _userMarkerList.value = markerList
        }

    }

    fun addLocation(map: Map) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.addLocation(map)

            when (result) {
                is AppResult.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
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


}
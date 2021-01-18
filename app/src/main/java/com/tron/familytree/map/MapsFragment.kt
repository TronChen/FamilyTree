package com.tron.familytree.map

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.tron.familytree.R
import com.tron.familytree.data.Map
import com.tron.familytree.databinding.FragmentMapsBinding
import com.tron.familytree.databinding.ItemMapPinBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.util.UserManager


class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    // permission request code, just is a Int and unique.
    var PERMISSION_ID = 1010
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermission = false
    private var myMap: GoogleMap? = null
    private var lastKnownLocation: Location? = null


    private val callback = OnMapReadyCallback { googleMap ->
        myMap = googleMap
        getLocationPermission()

        myMap?.setOnMarkerClickListener(this)

        googleMap?.apply {
            uiSettings.isMyLocationButtonEnabled = true
        }

        getDeviceLocation()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val viewModel by viewModels<MapsViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMapsBinding.inflate(inflater)
        binding.viewModel = viewModel

        viewModel._userLocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {userLocationList ->
            viewModel.liveUserLocation.value = userLocationList
        })

        viewModel._userLocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer { userLocationList ->

            val familyMember = userLocationList.filter {
                it.familyId == viewModel.user.value?.familyId
                        && it.familyId != null
            }

            myMap?.let { map ->
                viewModel.drawUsersLocation(map,familyMember)
            }
            Log.e("familyMemberMap",familyMember.toString())
            Log.e("userLocationList",userLocationList.toString())
        })

        viewModel.userTag.observe(viewLifecycleOwner, Observer {
            Log.e("userTag", it.toString())
            findNavController().navigate(MapsFragmentDirections.actionGlobalBranchUserDetailDialog(it))
        })

        viewModel.episodeTag.observe(viewLifecycleOwner, Observer {
            Log.e("userTag", it.toString())
            findNavController().navigate(MapsFragmentDirections.actionGlobalEpisodeDetailDialog(it))
        })

        binding.cardMyLocation.setOnClickListener {
            if (locationPermission){
                myMap?.apply {
                    moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 15f))
                }
            }
            if (!locationPermission){
                Toast.makeText(context,getString(R.string.noLocationPermission),Toast.LENGTH_SHORT).show()
            }
        }

        binding.cardEpisode.setOnClickListener {
            if (locationPermission) {
                val markerList = mutableListOf<Marker>()
                val bindingMapPin = ItemMapPinBinding.inflate(inflater)
                val map_pin = createDrawableFromView(requireContext(), bindingMapPin.imageView23)
                for (episode in viewModel.episode.value!!) {
                    myMap?.apply {
                        val marker = addMarker(
                            MarkerOptions()
                                .position(LatLng(episode.latitude!!, episode.longitude!!))
                                .icon(BitmapDescriptorFactory.fromBitmap(map_pin))
                        )
                        marker.tag = episode.id
                        markerList.add(marker)
                        Log.e("MarkerTag", marker.tag as String)
                    }
                }
                viewModel._episodeMarkerList.value = markerList
                viewModel._userMarkerList.value?.forEach { it.remove() }
            }
            if (!locationPermission){
                Toast.makeText(context,getString(R.string.noLocationPermission),Toast.LENGTH_SHORT).show()
            }
        }

        binding.cardMember.setOnClickListener {
            if (locationPermission) {
                viewModel.getUserLocation()
                getDeviceLocation()
                viewModel._episodeMarkerList.value?.forEach { it.remove() }
            }
            if (!locationPermission){
                Toast.makeText(context,getString(R.string.noLocationPermission),Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermission = false
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
        } else {
            locationPermission = true
        }
    }

    // 3. set map UI isMyLocationButton Enabled
    private fun updateLocationUI() {
        myMap?.apply {
            try {
                if (locationPermission) {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                } else {
                    isMyLocationEnabled = false
                    uiSettings.isMyLocationButtonEnabled = false
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    // 4. get permission and update LocationUI: set map UI isMyLocationButton Enabled
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ID -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = true
                    // set map UI isMyLocationButton Enabled
                    updateLocationUI()
                }
            }
        }
    }

    // 5. getDeviceLocation
    private fun getDeviceLocation() {
        try {
            if (locationPermission) {
                val locationRequest = fusedLocationProviderClient.lastLocation
                locationRequest.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {

                            myMap?.apply {


                                val myLocation = listOf(Map(
                                    longitude = lastKnownLocation!!.longitude,
                                    latitude = lastKnownLocation!!.latitude,
                                    userImage = UserManager.photo.toString(),
                                    userId = UserManager.email.toString()))

                                val myNowLocation = MutableLiveData<Map>()

                                for (index in myLocation){
                                    myNowLocation.value = index
                                }

                                viewModel.drawUsersLocation(this,myLocation)
                                viewModel.addLocation(myNowLocation.value!!)

                                moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 15f))
//                                })
                            }
                        }
                    } else {
                        myMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }



    override fun onMarkerClick(p0: Marker?): Boolean {
        viewModel._userMarkerList.observe(viewLifecycleOwner, Observer {
            Log.e("mark", it.toString())
            it.let {
                it.forEach {marker ->
                    myMap.let {
                        Log.e("_userMarkerList", marker.tag.toString())
                        if (p0 != null) {
                            if (p0.tag == UserManager.email) {
//                                Toast.makeText(context, "我在這兒", Toast.LENGTH_SHORT).show()
                            }
                            if (p0.tag == marker.tag) {
//                                Toast.makeText(context, "${p0.tag}", Toast.LENGTH_SHORT).show()
                                viewModel.findUserByIdTag(p0.tag.toString())
                            }
                        }

                    }
                }
            }
        })

        viewModel._episodeMarkerList.observe(viewLifecycleOwner, Observer {
            Log.e("mark", it.toString())
            it.let {
                it.forEach {marker ->
                    myMap.let {

                        if (p0 != null) {
                            if (p0.tag == marker.tag) {
//                                Toast.makeText(context, "${p0.tag}", Toast.LENGTH_SHORT).show()
                                viewModel.findEpisodeById(marker.tag.toString())
                            }
                        }

                    }
                }
            }
        })

        Log.e("MarkerClick", p0.toString())

        return false
    }

    fun createDrawableFromView(context: Context, view: View): Bitmap? {

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            1650,
            1650,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap.scale(150,150)
    }


}

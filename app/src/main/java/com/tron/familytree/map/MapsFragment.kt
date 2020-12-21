package com.tron.familytree.map

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.tron.familytree.R
import com.tron.familytree.data.Map
import com.tron.familytree.databinding.FragmentMapsBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.util.UserManager
import okhttp3.internal.notify


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
            myMap?.let { map ->
                viewModel.drawUsersLocation(map,userLocationList)
            }
        })

        binding.cardMyLocation.setOnClickListener {
            myMap?.apply {
                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 15f))
            }
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel._userMarkerList.value?.forEach { it.remove() }
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

                        if (p0 != null) {
                            if (p0.tag == UserManager.email) {
                                Toast.makeText(context, "${p0.tag}", Toast.LENGTH_SHORT).show()
                            }
                            if (p0.tag == marker.tag) {
                                Toast.makeText(context, "${p0.tag}", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }
        })
//        if (p0 != null) {
//
//            if(p0.tag == UserManager.email.toString())
//                Toast.makeText(requireContext(), "Hamdy", Toast.LENGTH_SHORT).show()
//        }

        Log.e("MarkerClick", p0.toString())

        return false
    }

//    override fun onMyLocationChange(p0: Location?) {
//        myMap?.apply {
//
//            Log.e("onMyLocationChange", p0.toString())
//
//            val myLocation = listOf(
//                Map(
//                    longitude = p0!!.longitude,
//                    latitude = p0.latitude,
//                    userImage = UserManager.photo.toString(),
//                    userId = UserManager.email.toString()
//                )
//            )
//
//            val myNowLocation = MutableLiveData<Map>()
//
//            for (index in myLocation) {
//                myNowLocation.value = index
//            }
//
//            viewModel.drawUsersLocation(this, myLocation)
//            viewModel.addLocation(myNowLocation.value!!)
//        }
//    }

//    private fun getUsersLocation() {
//
//        myMap?.apply {
//            viewModel.liveUserLocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//                it.let { list ->
//                    list.forEach { map ->
//                        val bindingItem =
//                            ItemListMarkerBinding.inflate(LayoutInflater.from(context))
//                        if (map.userImage != null) {
//
//                            val imgUri = map.userImage?.toUri()?.buildUpon()?.scheme("https")
//                                ?.build()
//                            Glide.with(FamilyTreeApplication.INSTANCE.applicationContext)
//                                .asBitmap()
//                                .load(imgUri)
//                                .apply(
//                                    RequestOptions()
//                                        .transform(
//                                            CenterCrop(),
//                                            GlideCircleBorderTransform(135f, 0)
//                                        )
//                                )
//                                .into(object : SimpleTarget<Bitmap>(150, 150) {
//                                    override fun onResourceReady(
//                                        resource: Bitmap,
//                                        transition: Transition<in Bitmap>?
//                                    ) {
//                                        addMarker(
//                                            MarkerOptions()
//                                                .position(
//                                                    LatLng(
//                                                        lastKnownLocation!!.latitude,
//                                                        lastKnownLocation!!.longitude
//                                                    )
//                                                )
//                                                .snippet("${lastKnownLocation!!.latitude}, ${lastKnownLocation!!.longitude}")
//                                                .icon(
//                                                    BitmapDescriptorFactory.fromBitmap(resource)
//                                                )
//                                        )
//                                    }
//
//                                })
//
//                        }
//
////                        addMarker(
////                            MarkerOptions()
////                                .position(LatLng(map.latitude!!, map.longitude!!))
////                                .title(map.userId)
////                                .snippet("${map.latitude!!},${map.longitude!!}")
////                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.people))
////                        )
//                    }
//                }
//            })
//        }
//    }
//            val userA = LatLng(25.0250383, 121.5327086)
//            val userB = LatLng(25.1714657, 121.4359783)
//            val userC = LatLng(25.0669043, 121.469388)
//            val userList = listOf(userA, userB, userC)
//
//            addMarker(MarkerOptions()
//                .position(userA)
//                .title("匿名蠑螈")
//                .snippet("${userA.latitude}, ${userA.longitude}")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))
//
//            addMarker(MarkerOptions()
//                .position(userB)
//                .title("匿名海豹")
//                .snippet("${userB.latitude}, ${userB.longitude}")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))
//
//            addMarker(MarkerOptions()
//                .position(userC)
//                .title("匿名喵喵")
//                .snippet("${userC.latitude}, ${userC.longitude}")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))
//        }



//    fun getBitmapFromView(view: View): Bitmap? {
//        val returnedBitmap =
//            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(returnedBitmap)
//        val bgDrawable = view.background
//        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
//        view.draw(canvas)
//        return returnedBitmap
//    }
//
//    fun createDrawableFromView(context: Context, view: View): Bitmap? {
//
//        val displayMetrics = DisplayMetrics()
//        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
//        view.layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
//        view.buildDrawingCache()
//        val bitmap = Bitmap.createBitmap(
//            1500,
//            1500,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        view.draw(canvas)
//        return bitmap.scale(120,120)
//    }


}

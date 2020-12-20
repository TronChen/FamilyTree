package com.tron.familytree.map

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tron.familytree.R
import com.tron.familytree.data.Map
import com.tron.familytree.databinding.FragmentMapsBinding
import com.tron.familytree.databinding.ItemListMarkerBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.util.UserManager


class MapsFragment : Fragment() {

    // permission request code, just is a Int and unique.
    var PERMISSION_ID = 1010
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermission = false
    private var myMap: GoogleMap? = null
    private var lastKnownLocation: Location? = null


    private val callback = OnMapReadyCallback { googleMap ->
        myMap = googleMap
        getLocationPermission()

        val spotA = LatLng(25.0424825, 121.5626907)
        val spotB = LatLng(25.0476935, 121.5152081)
        val spotC = LatLng(25.0774806, 121.2331741)
        val spotD = LatLng(25.1763029, 121.5462675)
        val spotList = listOf(spotA, spotB, spotC, spotD)

        googleMap?.apply {
//            for (spot in spotList) {
//                addMarker(MarkerOptions()
//                    .position(spot)
//                    .title("Spot")
//                    .snippet("${spot.latitude}, ${spot.longitude}")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))
//            }
//            addPolyline(PolylineOptions()
//                .add(spotA, spotB)
//                .color(0xFF2286c3.toInt())
//                .width(10F)
//                .pattern(listOf(Dot(),Gap(20F), Dash(40F), Gap(20F)))
//                .endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.circle))))
//
//            addPolyline(PolylineOptions()
//                .add(spotB, spotC)
//                .color(0xFF2286c3.toInt())
//                .width(10F)
//                .pattern(listOf(Dot(),Gap(20F), Dash(40F), Gap(20F)))
//                .endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.circle))))
//
//            addPolyline(PolylineOptions()
//                .add(spotC, spotD)
//                .color(0xFF2286c3.toInt())
//                .width(10F)
//                .pattern(listOf(Dot(),Gap(20F), Dash(40F), Gap(20F)))
//                .endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.circle))))

            moveCamera(CameraUpdateFactory.newLatLngZoom(spotA, 10F))

            uiSettings.isMyLocationButtonEnabled = true



        }


        //set friends Location
//        getUsersLocation()
        getDeviceLocation()


    }

    var ccc : Bitmap? = null
    private val viewModel by viewModels<MapsViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingItem = ItemListMarkerBinding.inflate(inflater)
        ccc =  createDrawableFromView(requireContext(),bindingItem.imageView8)

        val binding = FragmentMapsBinding.inflate(inflater)
        binding.viewModel = viewModel

//        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
//        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
                                Firebase.firestore.collection("Map").document(UserManager.email.toString())
                                    .set(Map(longitude = lastKnownLocation!!.longitude,
                                    latitude = lastKnownLocation!!.latitude,
                                    userImage = UserManager.photo.toString(),
                                    userId = UserManager.email.toString()))


                                addMarker(MarkerOptions()
                                    .position(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude))
                                    .title("It's ME!!")
                                    .snippet("${lastKnownLocation!!.latitude}, ${lastKnownLocation!!.longitude}")
                                    .icon(BitmapDescriptorFactory.fromBitmap(ccc)
                                    )
                                )

                                moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 10f))
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

//    private fun getUsersLocation() {
//
//        myMap?.apply {
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
//
//
//    }

    fun getBitmapFromView(view: View): Bitmap? {
        val returnedBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
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
            1500,
            1500,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap.scale(120,120)
    }


}

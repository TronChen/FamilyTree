package com.tron.familytree.map

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tron.familytree.R
import java.util.jar.Manifest


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


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /*val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney")
            .snippet("This is a pen"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

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


    val MY_PERMISSIONS_REQUEST_LOCATION = 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        }



        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
//        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                findNavController().navigate(R.id.action_global_mapsFragment)
//
//            } else {
//                Toast.makeText(requireContext(), "需要定位功能", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


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
                                addMarker(MarkerOptions()
                                    .position(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude))
                                    .title("It's ME!!")
                                    .snippet("${lastKnownLocation!!.latitude}, ${lastKnownLocation!!.longitude}")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))

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

    private fun getUsersLocation() {
        myMap?.apply {
            val userA = LatLng(25.0250383, 121.5327086)
            val userB = LatLng(25.1714657, 121.4359783)
            val userC = LatLng(25.0669043, 121.469388)
            val userList = listOf(userA, userB, userC)

            addMarker(MarkerOptions()
                .position(userA)
                .title("匿名蠑螈")
                .snippet("${userA.latitude}, ${userA.longitude}")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))

            addMarker(MarkerOptions()
                .position(userB)
                .title("匿名海豹")
                .snippet("${userB.latitude}, ${userB.longitude}")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))

            addMarker(MarkerOptions()
                .position(userC)
                .title("匿名喵喵")
                .snippet("${userC.latitude}, ${userC.longitude}")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle)))
        }


    }


}

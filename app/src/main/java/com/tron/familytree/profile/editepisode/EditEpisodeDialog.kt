package com.tron.familytree.profile.editepisode

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.Auth
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentEditEpisodeBinding
import com.tron.familytree.ext.getVmFactory
import java.util.*

const val PLACE_API = 1111

class EditEpisodeDialog : DialogFragment() {

    private val viewModel by viewModels<EditEpisodeViewModel> { getVmFactory(
        EditEpisodeDialogArgs.fromBundle(requireArguments()).episodeProperties)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_white_radius65)

        val binding = FragmentEditEpisodeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

//        Places.initialize(requireContext(),"AIzaSyDl_BDrZgG_D48maz3BHm0t95XhWwqcI98")
//        val placeClient : PlacesClient = Places.createClient(requireContext())
//
//        startAutoCompleteActivity(binding.editTextLocation)




        viewModel.editTitle.observe(viewLifecycleOwner, Observer {
            Log.e("EditTitle",it)
        })

        binding.conTIme.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(),{_, year, month, day ->
                viewModel.editDate.value ="${setDateFormat(year, month, day)}"
            },year,month,day).show()
        }

        binding.conConfirm.setOnClickListener {
            viewModel.addUserEpisode(viewModel.setEpisode())
            Log.e("Episode", viewModel.setEpisode().toString())
        }

        if (!Places.isInitialized()) {
            activity?.let { Places.initialize(it,"AIzaSyCiCPmbflbA9FlcM46aRJ4istK6GfAA2GQ") }
        }
// Create a new Places client instance.
// Create a new Places client instance.
        val placesClient = activity?.let { Places.createClient(it) }

        binding.editTextLocation.setOnClickListener {

            // Set the fields to specify which types of place data to return.

            val fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )

            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            ).setCountry("TW")
                .build(requireContext())
            startActivityForResult(intent, PLACE_API)
        }

















        return binding.root
    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year-${month + 1}-$day"
    }

    fun startAutoCompleteActivity(view:View){
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            Arrays.asList(Place.Field.ID, Place.Field.NAME)
        ).build(requireContext())

        startActivityForResult(intent,1010)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//    如果是的話 取得帳號資訊
        if (requestCode == PLACE_API) {

                Log.e("reqCode", requestCode.toString())
                val place = data?.let { Autocomplete.getPlaceFromIntent(it) }
                if (place != null) {
                    Log.e(
                        "PLACE_API",
                        "Place: " + place.name + ", " + place.id + ", " + place.address
                    )
                }
                if (place != null) {
                    Toast.makeText(
                        requireContext(),
                        "ID: " + place.id + "address:" + place.address + "Name:" + place.name + " latlong: " + place.latLng,
                        Toast.LENGTH_LONG
                    ).show()
                }
                val address = place?.address
                // do query with address
        }
    }

}
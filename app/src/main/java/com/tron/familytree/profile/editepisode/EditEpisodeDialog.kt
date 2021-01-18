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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.NavigationDirections
import com.tron.familytree.R
import com.tron.familytree.check.CheckDialog
import com.tron.familytree.databinding.FragmentEditEpisodeBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.util.UserManager
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
            if (
                viewModel.editTitle.value!! != "" &&
            viewModel.editDate.value!! != "" &&
            viewModel.editContent.value!! != "" &&
            viewModel.editLocation.value!! != "請選擇位址"){
                viewModel.addUserEpisode(viewModel.setEpisode())
                findNavController().navigate(
                    NavigationDirections.actionGlobalCheckDialog(
                        CheckDialog.MessageType.ADDED_SUCCESS))
                findNavController().navigateUp()
                Log.e("Episode", viewModel.setEpisode().toString())
            }else{
                Toast.makeText(requireContext(),"請輸入完整訊息", Toast.LENGTH_SHORT).show()
            }

        }

        if (!Places.isInitialized()) {
            activity?.let { Places.initialize(it,"AIzaSyCiCPmbflbA9FlcM46aRJ4istK6GfAA2GQ") }
        }

        // Create a new Places client instance.

        val placesClient = activity?.let { Places.createClient(it) }

        binding.textLocation.setOnClickListener {

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

        binding.imageCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year-${month + 1}-$day"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Get account info if true
        if (requestCode == PLACE_API) {

                Log.e("reqCode", requestCode.toString())
                val place = data?.let { Autocomplete.getPlaceFromIntent(it) }
            if (place != null) {
                viewModel.editLocation.value = place.name
                viewModel.latitude.value = place.latLng?.latitude
                viewModel.longitude.value = place.latLng?.longitude
            }
                if (place != null) {
                    Log.e(
                        "PLACE_API",
                        "Place: " + place.name + ", " + place.id + ", " + place.address
                    )
                }
            if (place != null) {
                Log.e("latLng", place.latLng?.latitude.toString())
            }
                // do query with address
        }
    }

}
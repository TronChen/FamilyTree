package com.tron.familytree.profile.edit_family.create_family

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.databinding.DialogCreateAlbumBinding
import com.tron.familytree.databinding.FragmentCreateFamilyBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.create_album.CreateAlbumViewModel

class CreateFamilyFragment : DialogFragment() {

    private val viewModel by viewModels<CreateFamilyViewModel> { getVmFactory() }

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

        val binding = FragmentCreateFamilyBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        binding.conOK.setOnClickListener {
            viewModel.editFamily.observe(viewLifecycleOwner, Observer {
                Log.e("editFamily",it)

                if (it == ""){
                    Toast.makeText(requireContext(),"請輸入家族名稱",Toast.LENGTH_SHORT).show()
                }

                if (viewModel.user.value?.familyId != null){
                    Toast.makeText(requireContext(),"你已經有家族了唷",Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.addFamily(viewModel.setFamily(), viewModel.user.value!!)
                    findNavController().navigate(R.id.action_global_editFamilyFragment)
                }

            })
        }



        binding.conCancel.setOnClickListener {
            findNavController().navigateUp()
        }


        // Inflate the layout for this fragment
        return binding.root
    }
}
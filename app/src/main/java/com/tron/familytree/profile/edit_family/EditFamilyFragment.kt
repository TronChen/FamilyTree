package com.tron.familytree.profile.edit_family

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentEditFamilyBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.edit_family.create_family.CreateFamilyAdapter


class EditFamilyFragment : Fragment() {

    private val viewModel by viewModels<EditFamilyViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditFamilyBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapterFamily = EditFamilyAdapter(EditFamilyAdapter.EditFamilyOnItemClickListener{
            if (viewModel.user.value?.familyId == null) {
                viewModel.updateFamily(it, viewModel.user.value!!)
            }
            else{
                Toast.makeText(requireContext(),"你已經有家族了唷", Toast.LENGTH_SHORT).show()
            }
        },viewModel)


        binding.recFamily.adapter = adapterFamily

        binding.conAddFamily.setOnClickListener {
            findNavController().navigate(R.id.action_global_createFamilyFragment)
        }

        viewModel.liveFamily.observe(viewLifecycleOwner, Observer {
            Log.e("FamilyList", it.toString())
            adapterFamily.submitList(it)
        })



        return binding.root
    }
}
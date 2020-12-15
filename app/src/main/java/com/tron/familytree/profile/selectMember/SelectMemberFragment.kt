package com.tron.familytree.profile.selectMember

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentQrCodeReaderBinding
import com.tron.familytree.databinding.FragmentSelectMemberBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.qrcode.QrCodeReaderFragmentArgs
import com.tron.familytree.profile.qrcode.QrCodeReaderViewModel

class SelectMemberFragment : Fragment() {

    private val viewModel by viewModels<SelectMemberViewModel> { getVmFactory(
        SelectMemberFragmentArgs.fromBundle(
            requireArguments()
        ).userProperties)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSelectMemberBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = SelecMemberAdapter(SelecMemberAdapter.SelecMemberOnItemClickListener{
            Log.e("SelectMember", it.toString())
            when (viewModel.selectedProperty.value?.name){

                "No mate" ->{
                    viewModel.updateMemberMateId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }

                "No father" -> {
                    viewModel.updateMemberFatherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No mother" -> {
                    viewModel.updateMemberMotherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No mateFather" -> {
                    viewModel.updateMemberFatherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No mateMother" -> {
                    viewModel.updateMemberMotherId(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
                "No child" -> {
                    viewModel.updateChild(viewModel.selectedProperty.value!!, it)
                    findNavController().navigate(R.id.action_global_branchFragment)
                }
            }
        })
        binding.recMember.adapter = adapter

        viewModel.familyMember.observe(viewLifecycleOwner, Observer {
            Log.e("Member", it.toString())
            adapter.submitList(it)
        })

        viewModel.selectedProperty.observe(viewLifecycleOwner, Observer {
            Log.e("WhoNeedPaPa", it.toString())
        })


        return binding.root
    }
}
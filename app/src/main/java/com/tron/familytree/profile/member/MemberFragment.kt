package com.tron.familytree.profile.member

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentMemberBinding
import com.tron.familytree.databinding.FragmentProfileBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.ProfileViewModel


class MemberFragment() : Fragment() {

    var type: Int = 0

    constructor(int : Int) : this() {
        this.type = int
    }
    private val viewModel by viewModels<MemberViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMemberBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = MemberAdapter(MemberAdapter.UserOnItemClickListener{
            Log.e("User", it.toString())
            if(it.name == "加入父親" || it.name == "No father"){
                it.name = "No father"
                findNavController().navigate(MemberFragmentDirections.actionGlobalAddPeopleDialog(it))
            }
            if(it.name == "加入母親" || it.name == "No mother"){
                it.name = "No mother"
                findNavController().navigate(MemberFragmentDirections.actionGlobalAddPeopleDialog(it))
            }
            if(it.name == "加入配偶" || it.name == "No mate"){
                it.name = "No mate"
                findNavController().navigate(MemberFragmentDirections.actionGlobalAddPeopleDialog(it))
            }
            if(it.name == "加入孩子" || it.name == "No child"){
                it.name = "No child"
                findNavController().navigate(MemberFragmentDirections.actionGlobalAddPeopleDialog(it))
            }
        })

        binding.recyclerMember.adapter = adapter


        viewModel.adapterList.observe(viewLifecycleOwner, Observer {
            Log.e("Member", it.toString())
            adapter.submitList(it)
        })

        return binding.root
    }

}
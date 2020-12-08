package com.tron.familytree.profile.member

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member, container, false)
    }

}
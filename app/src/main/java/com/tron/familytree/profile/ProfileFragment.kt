package com.tron.familytree.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.tron.familytree.R
import com.tron.familytree.branch.BranchViewModel
import com.tron.familytree.databinding.FragmentProfileBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.FamilyViewModel
import com.tron.familytree.profile.viewpager.ProfilePagerAdapter

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val tabLayout = binding.tabs
        val viewPager = binding.viewpager
        viewPager.adapter = ProfilePagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position){
                0 -> "生平事蹟"
                1 -> "家族成員"
                else -> null
            }
        }.attach()



        viewModel.user.observe(viewLifecycleOwner, Observer {
        requireActivity().findViewById<ImageView>(R.id.imageEdit).setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionGlobalEditUserFragment(
                viewModel.user.value!!
            ))
        }
        })

        viewModel._family.observe(viewLifecycleOwner, Observer {
            Log.e("family", it.toString())
             viewModel.familyName = it.title
        })

        return binding.root
    }
}
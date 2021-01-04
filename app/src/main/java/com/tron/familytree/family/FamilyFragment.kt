package com.tron.familytree.family

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.tron.familytree.databinding.FragmentFamilyBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.viewpager.FamilyViewPagerAdapter

class FamilyFragment : Fragment() {

    private val viewModel by viewModels<FamilyViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFamilyBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel





        val tabLayout = binding.tabs
        val viewPager = binding.viewpager
        viewPager.adapter = FamilyViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position){
                0 -> "即將開始"
                1 -> "已完成"
                else -> null
            }
        }.attach()


        return binding.root
    }
}
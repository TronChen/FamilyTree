package com.tron.familytree.profile.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tron.familytree.message.MessageFragment
import com.tron.familytree.profile.ProfileFragment

class ProfilePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {

            return when(position){
//                0 -> ViewpagerCatalogItemFragment(0)
//                1 -> ViewpagerCatalogItemFragment(1)
                else -> MessageFragment()
            }
        }
    }

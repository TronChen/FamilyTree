package com.tron.familytree.profile.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tron.familytree.message.MessageFragment
import com.tron.familytree.profile.ProfileFragment
import com.tron.familytree.profile.episode.EpisodeFragment
import com.tron.familytree.profile.member.MemberFragment

class ProfilePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> EpisodeFragment(0)
                1 -> MemberFragment(1)
                else -> MessageFragment()
            }
        }
    }

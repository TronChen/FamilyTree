package com.tron.familytree.family.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tron.familytree.family.album.AlbumFragment
import com.tron.familytree.family.event.EventFragment
import com.tron.familytree.message.MessageFragment
import com.tron.familytree.profile.episode.EpisodeFragment
import com.tron.familytree.profile.member.MemberFragment

    class FamilyViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> EventFragment(0)
                1 -> AlbumFragment(1)
                else -> MessageFragment()
            }
        }
    }
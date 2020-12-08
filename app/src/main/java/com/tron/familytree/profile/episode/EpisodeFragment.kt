package com.tron.familytree.profile.episode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.FragmentEpisodeBinding
import com.tron.familytree.databinding.FragmentProfileBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.family.event.EventAdapter
import com.tron.familytree.profile.ProfileViewModel


class EpisodeFragment() : Fragment() {
    var type: Int = 0

    constructor(int : Int) : this() {
        this.type = int
    }


    private val viewModel by viewModels<EpisodeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEpisodeBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = EpisodeAdapter(EpisodeAdapter.EpisodeOnItemClickListener {
            Log.e("EventCLick", it.toString())
        })
        binding.recyclerEpisode.adapter = adapter

        adapter.submitList(createMock())



        return binding.root
    }


    fun createMock() : List<Episode>{
        val episode1 = Episode(
            user  = "茶川川",
            content = "當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學",
            title = "博士畢業",
            time = "1984",
            location = "台北"
        )

        val episode2 = Episode(
            user  = "茶川川",
            content = "沒那麼快樂了，累累博士生當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學當個快樂的博士生，每天睏霸上學",
            title = "雙博士畢業",
            time = "1986",
            location = "台北"
        )

        val list = mutableListOf<Episode>()
        list.add(episode1)
        list.add(episode2)
        list.add(episode1)
        list.add(episode2)
        list.add(episode1)
        list.add(episode2)
        list.add(episode1)
        list.add(episode2)

        return list
    }
}
package com.tron.familytree.branch.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tron.familytree.data.Episode
import com.tron.familytree.databinding.DialogBranchUserDetailBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.episode.EpisodeAdapter

class BranchUserDetailDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<BranchUserDetailDialogViewModel> {  getVmFactory(
        BranchUserDetailDialogArgs.fromBundle(
            requireArguments()
        ).userProperties)}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogBranchUserDetailBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this

//        val user = BranchUserDetailDialogArgs.fromBundle(
//            requireArguments()
//        ).userProperties
//        Log.e(
//            "userProperties",
//            "BranchUserDetailDialogArgs.fromBundle(requireArguments()).userProperties = $user"
//        )

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
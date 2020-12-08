package com.tron.familytree.profile.edituser

import android.app.AlertDialog
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
import com.tron.familytree.databinding.FragmentEditUserBinding
import com.tron.familytree.ext.getVmFactory

class EditUserFragment : Fragment() {

    private val viewModel by viewModels<EditUserViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditUserBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = EditEpisodeAdapter(
            EditEpisodeAdapter.EditEpisodeOnItemClickListener {
                Log.e("EditEpisodeClick", it.toString())
            })

        binding.recyclerEditEpisode.adapter = adapter

        adapter.submitList(createMock())





        binding.conAddEpisode.setOnClickListener {
            findNavController().navigate(R.id.action_global_editEpisodeDialog)
//            addToCart()
        }


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


    private fun addToCart() {
        val addToCart = LayoutInflater.from(context).inflate(R.layout.fragment_edit_episode, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(addToCart)
        val addToCartSuccess = mBuilder.show()
        addToCartSuccess.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}


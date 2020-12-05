package com.tron.familytree.profile.episode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tron.familytree.R


class EpisodeFragment() : Fragment() {
    var type: Int = 0

    constructor(int : Int) : this() {
        this.type = int
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }
}
package com.tron.familytree.profile.episode.episode_dialog

import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.Episode

class EpisodeDetailViewModel(
    private val repository: FamilyTreeRepository
    , private val episode: Episode
) : ViewModel() {

}
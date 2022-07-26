package com.premelc.shows_dominik_premelc.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.ShowsObject
import com.premelc.shows_dominik_premelc.ShowsObject.showsList
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show

class ShowsViewModel : ViewModel() {

    private val _shows = MutableLiveData<List<Show>>()
    val shows: LiveData<List<Show>> = _shows

    init {
        _shows.value = showsList
    }
}
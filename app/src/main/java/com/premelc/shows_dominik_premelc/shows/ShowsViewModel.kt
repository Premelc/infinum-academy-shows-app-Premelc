package com.premelc. shows_dominik_premelc.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premelc.shows_dominik_premelc.ShowsObject.showsList
import com.premelc.shows_dominik_premelc.model.Show

class ShowsViewModel : ViewModel() {

    private val _shows = MutableLiveData<List<Show>>()
    val shows: LiveData<List<Show>> = _shows

    private val _showsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    val showsRecyclerFullOrEmpty:LiveData<Boolean> = _showsRecyclerFullOrEmpty

    init {
        _shows.value = showsList
        _showsRecyclerFullOrEmpty.value = shows.value?.size!! > 0
    }
}
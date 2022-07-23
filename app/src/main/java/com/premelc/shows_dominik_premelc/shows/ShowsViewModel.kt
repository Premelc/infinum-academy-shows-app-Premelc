package com.premelc.shows_dominik_premelc.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show

class ShowsViewModel: ViewModel() {
    private val reviews: List<Review> = listOf(
        Review(
            "petra_benjak",
            "Petra Benjak",
            5F,
            "Najbolja stvar koju sam ikad gledala",
            R.mipmap.pfp
        ),
        Review(
            "premo",
            "Premo",
            4F,
            "Najbolja stvar koju sam ikad gledao",
            R.mipmap.pfp
        ),
        Review(
            "zigmund123",
            "zigmund123",
            2F,
            "ne kuzim",
            R.mipmap.pfp
        )
    )

    private val showsList: List<Show> = listOf(
        Show(
            "the_office",
            "The Office",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            reviews,
            R.mipmap.the_office
        ),
        Show(
            "stranger_things",
            "Stranger Things",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            reviews,
            R.mipmap.stranger_things
        ),
        Show(
            "krv_nije_voda",
            "Krv nije voda",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            reviews,
            R.mipmap.krv_nije_voda
        )
    )
    private val _shows = MutableLiveData(showsList)
    val shows: LiveData<List<Show>> = _shows

    fun fetchShows(): List<Show>{
        _shows.value = showsList
        return _shows.value!!
    }

   fun findShowById(id: String): Show? {
       var retval: Show? = null
       for (item in _shows.value!!) {
           if (item.id == id) {
               retval = item
           }
       }
       return retval
   }
}
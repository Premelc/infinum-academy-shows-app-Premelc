package com.premelc.shows_dominik_premelc.showDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premelc.shows_dominik_premelc.ShowsObject.findShowById
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show
import java.text.DecimalFormat

class ShowDetailsViewModel : ViewModel() {
    private var _show = MutableLiveData<Show>()
    val show: LiveData<Show> = _show

    private var _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private var _reviewCount = MutableLiveData<Int>()
    val reviewCount: LiveData<Int> = _reviewCount

    private var _reviewAvg = MutableLiveData<Float>()
    val reviewAvg: LiveData<Float> = _reviewAvg

    private var _rating = MutableLiveData<Float>()
    val rating: LiveData<Float> = _rating

    private var _reviewsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    val reviewsRecyclerFullOrEmpty: LiveData<Boolean> = _reviewsRecyclerFullOrEmpty

    fun initDetails(id: String) {
        val show = findShowById(id)
        if (show != null) setShow(show)
    }

    private fun setShow(show: Show) {
        _show.value = show
        _reviews.value = show.reviews
    }

    fun addReview(review: Review) {
        _reviews.value = reviews.value?.plus(review)
    }

    fun initRatingDisplay() {
        var sum = 0F
        val df = DecimalFormat("#.##")
        val count = reviews.value!!.count()
        for (item in reviews.value!!) {
            sum += item.grade
        }
        val avg = df.format(sum / reviews.value!!.count())
        _reviewAvg.value = avg.toFloat()
        _reviewCount.value = count
        _rating.value = sum / reviews.value!!.count()
        reviewsRecyclerFullOrEmpty()
    }

    fun reviewsRecyclerFullOrEmpty(){
        _reviewsRecyclerFullOrEmpty.value = reviewCount.value!! <= 0
    }


}
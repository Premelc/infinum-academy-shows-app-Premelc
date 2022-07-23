package com.premelc.shows_dominik_premelc.showDetails

import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show
import java.text.DecimalFormat

class ShowDetailsViewModel: ViewModel() {
    private var _show = MutableLiveData(Show("plc" , "plc" ,"plc", emptyList<Review>() , R.mipmap.pfp))
    val show: LiveData<Show> = _show

    private var _reviewCount = MutableLiveData<Int>(1)
    val reviewCount: LiveData<Int> = _reviewCount

    private var _reviewAvg = MutableLiveData<Float>(1.0F)
    val reviewAvg: LiveData<Float> = _reviewAvg

    private var _rating = MutableLiveData<Float>(1.0F)
    val rating: LiveData<Float> = _rating

    fun setShow(show:Show){
        _show.value = show
    }

    fun initRatingDisplay(reviews:List<Review> , rating: RatingBar, reviewsNumber: TextView){
        var sum = 0F
        val df = DecimalFormat("#.##")
        val count = reviews.count()
        for (item in reviews) {
            sum += item.grade
        }
        val avg = df.format(sum / reviews.count())
        _reviewAvg.value = avg.toFloat()
        _reviewCount.value = count
        _rating.value = sum / reviews.count()
    }
}
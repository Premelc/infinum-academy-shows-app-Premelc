package com.premelc.shows_dominik_premelc.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.premelc.shows_dominik_premelc.showDetails.ShowDetailsViewModel
import com.premelc.shows_dominik_premelc.shows.ShowsViewModel
import java.lang.IllegalArgumentException

class ShowsViewModelFactory(val database: ShowsDatabase): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass.isAssignableFrom(ShowsViewModel::class.java)){
            return ShowsViewModel(database) as T
        }else if(modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)){
            return ShowDetailsViewModel(database) as T
        }
            throw IllegalArgumentException("Unable to work with given view model")
    }

}
package com.premelc.shows_dominik_premelc.shows.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
import com.premelc.shows_dominik_premelc.model.Show
import java.io.File
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType

val MEDIA_TYPE_JPG = "image/png".toMediaType()

class ShowsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val repo = ShowsViewModelRepository(database)
    val shows: LiveData<List<Show>> = repo.getShows()
    val showsRecyclerFullOrEmpty: LiveData<Boolean> = repo.getShowsRecyclerFullOrEmpty()
    val showsResponse: LiveData<Boolean> = repo.getShowsResponse()
    val showsErrorMessage: LiveData<String> = repo.getShowsErrorMessage()
    val changePhotoResponse: LiveData<Boolean> = repo.getChangePhotoResponse()
    val changePhotoResponseMessage: LiveData<String> = repo.getChangePhotoResponseMessage()
    var connectionEstablished: LiveData<Boolean> = repo.getConnectionEstablished()

    fun initialFetchShows() {
        repo.initialFetchShows()
    }

    fun fetchShows() {
        viewModelScope.launch {
            repo.fetchShows()
        }
    }

    fun submitPendingReviews(userEmail: String) {
        repo.submitPendingReviews(userEmail)
    }

    fun fetchTopRatedShows() {
        viewModelScope.launch {
            repo.fetchTopRatedShows()
        }
    }

    fun uploadImage(email: String, file: File) {
        repo.uploadImage(email, file)
    }

    fun loadShowsToDb(shows: List<Show>) {
        viewModelScope.launch {
            repo.loadShowsToDb(shows)
        }
    }
}
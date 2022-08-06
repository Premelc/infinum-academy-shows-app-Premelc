package com.premelc.shows_dominik_premelc.shows.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
import com.premelc.shows_dominik_premelc.model.Show
import java.io.File
import kotlinx.coroutines.launch

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
    val connectionEstablished: LiveData<Boolean> = repo.getConnectionEstablished()
    val reviewForDeletion: LiveData<String> = repo.getReviewForDeletion()

    fun fetchShows() {
        viewModelScope.launch {
            repo.fetchShows()
        }
    }

    fun deleteReview(id: String) {
        viewModelScope.launch {
            repo.deleteReview(id)
        }
    }

    fun submitPendingReviews(userEmail: String) {
        viewModelScope.launch {
            repo.submitPendingReviews(userEmail)
        }
    }

    fun fetchTopRatedShows() {
        viewModelScope.launch {
            repo.fetchTopRatedShows()
        }
    }

    fun uploadImage(email: String, file: File) {
        repo.uploadImageToServer(email, file)
    }

    fun loadShowsToDb(shows: List<Show>) {
        viewModelScope.launch {
            repo.loadShowsToDb(shows)
        }
    }
}
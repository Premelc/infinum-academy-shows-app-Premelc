package com.premelc.shows_dominik_premelc.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ActivityShowsBinding
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show

class ShowsActivity : AppCompatActivity() {

    companion object {
        fun buildShowsActivityIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }
    }

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter
    private lateinit var shows: List<Show>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shows = ListOfShows().shows
        val username = intent.extras?.getString("username")
        initShowsRecycler(username)
        initRecyclerToggleButton()
    }

    private fun initShowsRecycler(username: String?) {
        val clickHandler: (v: View, id: String, username: String) -> Unit = { view: View, id: String, username: String ->
            val intent = ShowDetailsActivity.buildShowDetailsActivityIntent(view.context as Activity)
            println("IDDEEEEEEE: " + id)
            intent.putExtra("id", id)
            intent.putExtra("username", username)
            view.context.startActivity(intent)
        }
        adapter = ShowsAdapter(emptyList(), username.toString(), clickHandler)
        binding.showsRecycler.layoutManager = LinearLayoutManager(this)
        binding.showsRecycler.adapter = adapter
        adapter.addAllShows(shows)
        setShowsRecyclerFullOrEmpty(true)
    }

    private fun initRecyclerToggleButton() {
        binding.recyclerToggleButton.setOnClickListener {
            setShowsRecyclerFullOrEmpty(binding.emptyState.isVisible)
        }
    }

    private fun setShowsRecyclerFullOrEmpty(isEmpty: Boolean) {
        binding.showsRecycler.isVisible = isEmpty
        binding.emptyStateElipse.isVisible = !isEmpty
        binding.emptyStateIcon.isVisible = !isEmpty
        binding.emptyState.isVisible = !isEmpty
    }
}
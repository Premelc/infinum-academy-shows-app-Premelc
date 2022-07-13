package com.premelc.shows_dominik_premelc.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ActivityShowsBinding
import com.premelc.shows_dominik_premelc.model.Show

class ShowsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, ShowsActivity::class.java)
        }
    }

    private val shows = listOf(
        Show(
            "the_office",
            "The Office",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.the_office
        ),
        Show(
            "stranger_things",
            "Stranger Things",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.stranger_things
        ),
        Show(
            "krv_nije_voda",
            "Krv nije voda",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.krv_nije_voda
        )
    )

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initShowsRecycler()
        initEmptyRecyclerButton()
        initFillRecyclerButton()
    }

    private fun initShowsRecycler() {
        adapter = ShowsAdapter(emptyList()){}
        binding.showsRecycler.layoutManager = LinearLayoutManager(this)
        binding.showsRecycler.adapter = adapter
        /*binding.showsRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )*/
        adapter.addAllShows(shows)
        toggleEmptyState(false)
    }

    private fun initFillRecyclerButton(){
        binding.fillRecyclerButton.setOnClickListener {
            adapter.addAllShows(shows)
            toggleEmptyState(false)
        }
    }

    private fun initEmptyRecyclerButton() {
        binding.emptyRecyclerButton.setOnClickListener {
            adapter.addAllShows(emptyList())
            toggleEmptyState(true)
        }
    }

    private fun toggleEmptyState(state: Boolean) {
        binding.showsRecycler.isVisible = !state
        binding.emptyStateElipse.isVisible = state
        binding.emptyStateIcon.isVisible = state
        binding.emptyState.isVisible = state
    }
}
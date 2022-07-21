package com.premelc.shows_dominik_premelc.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.premelc.shows_dominik_premelc.databinding.FragmentShowsBinding
import com.premelc.shows_dominik_premelc.model.Show

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowsFragmentArgs>()
    private lateinit var adapter: ShowsAdapter
    private lateinit var shows: List<Show>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        shows = ListOfShows().shows
        initRecyclerToggleButton()
        initShowsRecycler()
        initLogout()
    }

    private fun initShowsRecycler() {
        val clickHandler: (id: String) -> Unit = { id: String ->
            val directions = ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(id, args.username)
            findNavController().navigate(directions)
        }
        adapter = ShowsAdapter(shows, clickHandler)
        binding.showsRecycler.layoutManager = LinearLayoutManager(context)
        binding.showsRecycler.adapter = adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLogout() {
        binding.logout.setOnClickListener {
            val directions = ShowsFragmentDirections.actionShowsFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
    }
}
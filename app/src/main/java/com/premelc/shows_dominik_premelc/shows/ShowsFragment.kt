package com.premelc.shows_dominik_premelc.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.FragmentShowsBinding
import com.premelc.shows_dominik_premelc.login.LoginFragment
import com.premelc.shows_dominik_premelc.model.Show

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private  val binding get() = _binding!!
    private val args by navArgs<ShowsFragmentArgs>()

    private lateinit var adapter: ShowsAdapter
    private lateinit var shows: List<Show>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment()
    }

    private fun initializeFragment(){
        shows = ListOfShows().shows
        val username = args.username
        initRecyclerToggleButton()
        initLogoutButton()
        initShowsRecycler(username)
    }

    private fun initShowsRecycler(username: String?) {
        val clickHandler: (id: String, username: String) -> Unit = { id: String, username: String ->
            /*
            var arguments = Bundle()
            arguments.putString("id" , id)
            arguments.putString("username",username)
            val showDetailsFragment = ShowDetailsFragment()
            showDetailsFragment.arguments = arguments
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                replace(R.id.main_container , showDetailsFragment)
                addToBackStack("Shows Fragment")
            }*/
            val directions = ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(id , username)
            findNavController().navigate(directions)
        }
        adapter = ShowsAdapter(emptyList(), username.toString(), clickHandler)
        binding.showsRecycler.layoutManager = LinearLayoutManager(context)
        binding.showsRecycler.adapter = adapter
        adapter.addAllShows(shows)
        setShowsRecyclerFullOrEmpty(true)
    }

    private fun initRecyclerToggleButton() {
        binding.recyclerToggleButton.setOnClickListener {
            setShowsRecyclerFullOrEmpty(binding.emptyState.isVisible)
        }
    }

    private fun initLogoutButton(){
        binding.logoutButton.setOnClickListener{
            /*activity?.supportFragmentManager?.commit {
                var loginFragment = LoginFragment()
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment , loginFragment)
            }*/
            val directions = ShowsFragmentDirections.actionShowsFragmentToLoginFragment()
            findNavController().navigate(directions)
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
}
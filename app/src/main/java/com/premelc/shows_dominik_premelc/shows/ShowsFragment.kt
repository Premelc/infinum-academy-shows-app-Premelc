package com.premelc.shows_dominik_premelc.shows

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.CameraGaleryBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.FragmentShowsBinding
import com.premelc.shows_dominik_premelc.databinding.ShowsBottomSheetBinding
import com.premelc.shows_dominik_premelc.model.Show

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowsFragmentArgs>()
    private lateinit var adapter: ShowsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var showsList: List<Show> = emptyList()
    private val viewModel by viewModels<ShowsViewModel>()

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.profileButton.setImageURI(uri)
            }
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { binding.profileButton.setImageURI(uri) }
    }

    private var latestTmpUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SHOWS", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shows.observe(viewLifecycleOwner) { shows ->
            showsList = shows
        }
        initializeUI()
    }

    private fun initializeUI() {
        initShowsRecycler()
        initProfileButton()
    }

    private fun initShowsRecycler() {
        val clickHandler: (id: String) -> Unit = { id: String ->
            val directions = ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(id, args.username)
            findNavController().navigate(directions)
        }
        showsList = viewModel.fetchShows()
        this.adapter = ShowsAdapter(showsList, clickHandler)
        binding.showsRecycler.layoutManager = LinearLayoutManager(context)
        binding.showsRecycler.adapter = this.adapter
        setShowsRecyclerFullOrEmpty(true)
    }

    private fun setShowsRecyclerFullOrEmpty(isEmpty: Boolean) {
        binding.showsRecycler.isVisible = isEmpty
        binding.emptyStateElipse.isVisible = !isEmpty
        binding.emptyStateIcon.isVisible = !isEmpty
        binding.emptyState.isVisible = !isEmpty
    }

    private fun initProfileButton() {
        binding.profileButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val bottomSheetBinding: ShowsBottomSheetBinding = ShowsBottomSheetBinding.inflate(layoutInflater)
            val bottomSheetBinding2: CameraGaleryBottomSheetBinding = CameraGaleryBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(bottomSheetBinding.root)
            val btnChangePhoto = bottomSheetBinding.changePhotoButton
            val btnLogout = bottomSheetBinding.logoutButton
            bottomSheetBinding.profilePic.setImageResource(R.mipmap.pfp)
            bottomSheetBinding.email.text = sharedPreferences.getString("EMAIL", "example@example.com")
            btnLogout.setOnClickListener {
                sharedPreferences.edit().clear().commit()
                val directions = ShowsFragmentDirections.actionShowsFragmentToLoginFragment()
                findNavController().navigate(directions)
                dialog.dismiss()
            }
            btnChangePhoto.setOnClickListener {
                dialog.setContentView(bottomSheetBinding2.root)
            }
            dialog.setContentView(bottomSheetBinding.root)
            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
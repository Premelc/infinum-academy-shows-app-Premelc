package com.premelc.shows_dominik_premelc.shows

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.BuildConfig.APPLICATION_ID
import com.premelc.shows_dominik_premelc.FileUtil.createImageFile
import com.premelc.shows_dominik_premelc.FileUtil.getImageFile
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.CameraGaleryBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.FragmentShowsBinding
import com.premelc.shows_dominik_premelc.databinding.ShowsBottomSheetBinding
import com.premelc.shows_dominik_premelc.model.Show
import java.io.File

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowsFragmentArgs>()
    private lateinit var adapter: ShowsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var showsList: List<Show> = emptyList()
    private val viewModel by viewModels<ShowsViewModel>()

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                Glide.with(requireContext())
                    .load(
                        ShowsFragment().getFileUri(
                            createImageFile(requireContext(), args.username),
                            requireContext()
                        )
                    )
                    .override(30, 30).error(
                        R.mipmap.pfp
                    ).into(binding.profileButton)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SHOWS", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            val directions =
                ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(id, args.username)
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
        Glide.with(requireContext())
            .load(
                ShowsFragment().getFileUri(
                    getImageFile(requireContext(), args.username),
                    requireContext()
                )
            )
            .override(30, 30).error(
                R.mipmap.pfp
            ).into(binding.profileButton)
        binding.profileButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            initProfileBottomSheet(dialog)
        }
    }

    private fun initProfileBottomSheet(dialog: BottomSheetDialog) {
        val bottomSheetBinding: ShowsBottomSheetBinding =
            ShowsBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
        Glide.with(requireContext())
            .load(
                ShowsFragment().getFileUri(
                    getImageFile(requireContext(), args.username),
                    requireContext()
                )
            )
            .override(100, 100).error(
                R.mipmap.pfp
            ).into(bottomSheetBinding.profilePic)
        bottomSheetBinding.email.text = sharedPreferences.getString("EMAIL", "example@example.com")
        bottomSheetBinding.logoutButton.setOnClickListener {
            initLogoutButton(dialog)
        }
        bottomSheetBinding.changePhotoButton.setOnClickListener {
            initChangePhotoButtons(dialog)
        }
        dialog.setContentView(bottomSheetBinding.root)
        dialog.show()
    }

    private fun initChangePhotoButtons(dialog: BottomSheetDialog) {
        val bottomSheetBinding2: CameraGaleryBottomSheetBinding =
            CameraGaleryBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding2.root)
        bottomSheetBinding2.cameraButton.setOnClickListener {
            takeImage()
            dialog.dismiss()
        }
        bottomSheetBinding2.galleryButton.setOnClickListener {
            Toast.makeText(context, R.string.wip, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initLogoutButton(dialog: BottomSheetDialog) {
        sharedPreferences.edit().clear().commit()
        val directions = ShowsFragmentDirections.actionShowsFragmentToLoginFragment()
        findNavController().navigate(directions)
        dialog.dismiss()
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getFileUri(
                createImageFile(requireContext(), args.username),
                requireContext()
            ).let { uri ->
                takeImageResult.launch(uri)
            }
        }
    }

    fun getFileUri(file: File?, context: Context): Uri? {
        if (file == null) return null
        return FileProvider.getUriForFile(context, "${APPLICATION_ID}.provider", file)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
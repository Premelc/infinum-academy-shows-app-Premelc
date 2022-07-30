package com.premelc.shows_dominik_premelc.shows

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.FileUtil.createImageFile
import com.premelc.shows_dominik_premelc.FileUtil.getFileUri
import com.premelc.shows_dominik_premelc.FileUtil.getImageFile
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.ShowApplication
import com.premelc.shows_dominik_premelc.databinding.CameraGaleryBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.FragmentShowsBinding
import com.premelc.shows_dominik_premelc.databinding.LoadingBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.RequestResponseBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.ShowsBottomSheetBinding
import com.premelc.shows_dominik_premelc.db.ShowsViewModelFactory
import com.premelc.shows_dominik_premelc.login.SHARED_PREFERENCES_ACCESS_TOKEN
import com.premelc.shows_dominik_premelc.login.SHARED_PREFERENCES_CLIENT
import com.premelc.shows_dominik_premelc.login.SHARED_PREFERENCES_EMAIL
import com.premelc.shows_dominik_premelc.login.SHARED_PREFERENCES_FILE_NAME
import com.premelc.shows_dominik_premelc.login.SHARED_PREFERENCES_PFP_URL
import com.premelc.shows_dominik_premelc.login.SHARED_PREFERENCES_TOKEN_TYPE
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.networking.ApiModule.initRetrofit

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowsFragmentArgs>()
    private lateinit var adapter: ShowsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var connectionEstablished = true
    private val viewModel:ShowsViewModel by viewModels{
        ShowsViewModelFactory((requireActivity().application as ShowApplication).database)
    }
    private lateinit var dialog: BottomSheetDialog

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                viewModel.uploadImage(args.username, getImageFile(requireContext())!!)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        initRetrofitHeader()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        dialog = BottomSheetDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shows.observe(viewLifecycleOwner) { shows ->
            adapter.addAllShows(shows)
            adapter.notifyDataSetChanged()
        }
        viewModel.showsRecyclerFullOrEmpty.observe(viewLifecycleOwner) { fullOrEmpty ->
            setShowsRecyclerFullOrEmpty(!fullOrEmpty)
        }
        viewModel.showsResponse.observe(viewLifecycleOwner) { showsResponse ->
            connectionEstablished = showsResponse
            dialog.dismiss()
            if (!showsResponse) {
                val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
                with(bottomSheetBinding) {
                    callbackIcon.setImageResource(R.drawable.fail)
                    callbackText.text = getString(R.string.shows_fetch_failed)
                    callbackDescription.text =  getString(R.string.connection_error)
                }
                dialog.setContentView(bottomSheetBinding.root)
                dialog.show()
            }
        }
        viewModel.showsErrorMessage.observe(viewLifecycleOwner){ showsErrorMessage->
            dialog.dismiss()
            val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
            with(bottomSheetBinding) {
                callbackIcon.setImageResource(R.drawable.fail)
                callbackText.text = getString(R.string.shows_fetch_failed)
                callbackDescription.text =  showsErrorMessage
            }
            dialog.setContentView(bottomSheetBinding.root)
            dialog.show()
        }
        viewModel.changePhotoResponse.observe(viewLifecycleOwner) { changePhotoResponse ->
            if (!changePhotoResponse) {
                val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
                with(bottomSheetBinding) {
                    callbackIcon.setImageResource(R.drawable.fail)
                    callbackText.text = getString(R.string.change_photo_error)
                    callbackDescription.text =  getString(R.string.connection_error)
                }
                dialog.setContentView(bottomSheetBinding.root)
                dialog.show()
            }
        }
        viewModel.changePhotoResponseMessage.observe(viewLifecycleOwner){changePhotoResponseMessage->
            val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
            if (URLUtil.isValidUrl(changePhotoResponseMessage)){
                sharedPreferences.edit()
                    .putString(SHARED_PREFERENCES_PFP_URL, changePhotoResponseMessage)
                    .commit()
                setProfilePicOnView(binding.profileButton)
                with(bottomSheetBinding) {
                    callbackIcon.setImageResource(R.drawable.success)
                    callbackText.text = getString(R.string.change_photo_success)
                    callbackDescription.text =  getString(R.string.empty)
                }
            }else{
                with(bottomSheetBinding) {
                    callbackIcon.setImageResource(R.drawable.fail)
                    callbackText.text = getString(R.string.change_photo_error)
                    callbackDescription.text =  changePhotoResponseMessage
                }
            }
            dialog.setContentView(bottomSheetBinding.root)
            dialog.show()
        }

        viewModel.fetchShowsFromDb().observe(viewLifecycleOwner){ showsFromDb ->
            if(!connectionEstablished){
                updateRecycler(showsFromDb.map { showEntity ->
                    Show(
                        showEntity.id,
                        showEntity.averageRating,
                        showEntity.description,
                        showEntity.imageUrl,
                        showEntity.noOfReviews,
                        showEntity.title
                    )
                })
                setShowsRecyclerFullOrEmpty(showsFromDb.isNotEmpty())
            }
        }
        initializeUI()
    }

    private fun initRetrofitHeader() {
        val header = mapOf(
            SHARED_PREFERENCES_TOKEN_TYPE to sharedPreferences.getString(SHARED_PREFERENCES_TOKEN_TYPE, "Bearer").toString(),
            SHARED_PREFERENCES_ACCESS_TOKEN to sharedPreferences.getString(SHARED_PREFERENCES_ACCESS_TOKEN, "default").toString(),
            SHARED_PREFERENCES_CLIENT to sharedPreferences.getString(SHARED_PREFERENCES_CLIENT, "default").toString(),
            SHARED_PREFERENCES_EMAIL to sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, "default@default.com").toString()
        )
        initRetrofit(requireContext(), header)
    }

    private fun initializeUI() {
        initLoadingBottomSheet()
        viewModel.fetchShowsFromServer()
        initShowsRecycler()
        initProfileButton()
        initTopRatedChip()
    }

    private fun initTopRatedChip() {
        val chip = binding.topRatedChip
        chip.setOnCheckedChangeListener{ chip: CompoundButton, chipIsChecked: Boolean ->
            if (chipIsChecked) viewModel.fetchTopRatedShowsFromServer()
            else viewModel.fetchShowsFromServer()
        }
    }

    private fun initLoadingBottomSheet() {
        val loadingBottomSheetBinding: LoadingBottomSheetBinding = LoadingBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(loadingBottomSheetBinding.root)
        dialog.show()
    }

    private fun initShowsRecycler() {
        val clickHandler: (id: String) -> Unit = { id: String ->
            val directions =
                ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(id, args.username)
            findNavController().navigate(directions)
        }
        adapter = ShowsAdapter(emptyList(), clickHandler)
        binding.showsRecycler.layoutManager = LinearLayoutManager(context)
        binding.showsRecycler.adapter = this.adapter
    }

    private fun setShowsRecyclerFullOrEmpty(isFull: Boolean) {
        binding.showsRecycler.isVisible = isFull
        binding.emptyStateElipse.isVisible = !isFull
        binding.emptyStateIcon.isVisible = !isFull
        binding.emptyState.isVisible = !isFull
    }

    private fun updateRecycler(list: List<Show>){
        adapter.addAllShows(list)
        adapter.notifyDataSetChanged()
    }

    private fun initProfileButton() {
        setProfilePicOnView(binding.profileButton)
        binding.profileButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            initProfileBottomSheet(dialog)
        }
    }

    private fun initProfileBottomSheet(dialog: BottomSheetDialog) {
        val bottomSheetBinding: ShowsBottomSheetBinding =
            ShowsBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
        setProfilePicOnView(bottomSheetBinding.profilePic)
        bottomSheetBinding.email.text = sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, "example@example.com")
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
        val cameraGalleryBottomSheetBinding: CameraGaleryBottomSheetBinding =
            CameraGaleryBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(cameraGalleryBottomSheetBinding.root)
        cameraGalleryBottomSheetBinding.cameraButton.setOnClickListener {
            takeImage()
            dialog.dismiss()
        }
        cameraGalleryBottomSheetBinding.galleryButton.setOnClickListener {
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
                createImageFile(requireContext()),
                requireContext()
            ).let { uri ->
                takeImageResult.launch(uri)
            }
        }
    }

    private fun setProfilePicOnView(view: ImageView) {
        val pfpUrl = sharedPreferences.getString(SHARED_PREFERENCES_PFP_URL, "default")
        Glide.with(requireContext())
            .load(
                pfpUrl
            )
            .placeholder(
                R.mipmap.pfp
            )
            .error(
                R.mipmap.pfp
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
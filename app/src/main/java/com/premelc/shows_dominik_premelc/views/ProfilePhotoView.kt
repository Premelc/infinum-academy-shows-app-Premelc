package com.premelc.shows_dominik_premelc.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ViewProfilePhotoBinding

class ProfilePhotoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit  var binding: ViewProfilePhotoBinding

    init {
        binding = ViewProfilePhotoBinding.inflate(LayoutInflater.from(context), this)
        clipChildren = false
        clipToPadding = false
    }

    fun setPicture(url: String) {
        Glide.with(binding.root.context)
            .load(
                url
            )
            .placeholder(
                R.mipmap.pfp
            )
            .error(
                R.mipmap.pfp
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.profilePic)
    }

    fun setDimensions(size: Int) {
        val picOutline = binding.profilePicHolder
        val pic = binding.profilePic
        var params = binding.profilePicHolder.layoutParams
        params.width = size + 4
        params.height = size + 4
        picOutline.layoutParams = params
        params = binding.profilePic.layoutParams
        params.width = size
        params.height = size
        pic.layoutParams = params
    }

}
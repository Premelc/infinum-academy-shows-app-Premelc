package com.premelc.shows_dominik_premelc.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.premelc.shows_dominik_premelc.databinding.ViewProfilePhotoBinding

class ProfilePhotoView @JvmOverloads  constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context,attrs,defStyleAttr){
    lateinit var binding: ViewProfilePhotoBinding

    init{
        binding = ViewProfilePhotoBinding.inflate(LayoutInflater.from(context),this)
        clipChildren = false
        clipToPadding = false
        setPadding(
            24,
            24,
            24,
            24
        )
    }

}
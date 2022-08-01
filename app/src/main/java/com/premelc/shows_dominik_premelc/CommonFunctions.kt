package com.premelc.shows_dominik_premelc

import com.premelc.shows_dominik_premelc.views.ProfilePhotoView

object CommonFunctions {

    fun resizeProfilePic(compView: ProfilePhotoView, size: Int) {
        val picOutline = compView.binding.profilePicHolder
        val pic = compView.binding.profilePic
        var params = compView.binding.profilePicHolder.layoutParams
        params.width = size + 4
        params.height = size + 4
        picOutline.layoutParams = params
        params = compView.binding.profilePic.layoutParams
        params.width = size
        params.height = size
        pic.layoutParams = params
    }
}
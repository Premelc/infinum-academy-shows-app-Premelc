package com.premelc.shows_dominik_premelc

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File

fun saveFile(source: Uri , context: Context){
    val sourceFilename = source.path
    val destinationFilename = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path+"avatar.jpg"

    val bis: BufferedInputStream? = null
    val bos: BufferedOutputStream? = null

    return
}
package com.spp.android.myapplication.xmlscreens.util.extensions

import android.widget.ImageView
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.spp.android.myapplication.R
import com.squareup.picasso.Picasso

object ImageViewExtensions {

    var currentImageLoader: ImageLoaderType = ImageLoaderType.COIL

    fun ImageView.loadAvatar(url: String?) {
        when (currentImageLoader) {
            ImageLoaderType.GLIDE -> loadWithGlide(url)
            ImageLoaderType.PICASSO -> loadWithPicasso(url)
            ImageLoaderType.COIL -> loadWithCoil(url)
        }
    }

    fun ImageView.loadWithGlide(url: String?) {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.drawable.baseline_account_circle_avatar)
            .error(android.R.drawable.ic_dialog_alert)
            .circleCrop()
            .into(this)
    }

    fun ImageView.loadWithPicasso(url: String?) {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.baseline_account_circle_avatar)
            .error(android.R.drawable.ic_dialog_alert)
            .transform(CircleTransform())
            .into(this)
    }

    private fun ImageView.loadWithCoil(url: String?) {
        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .placeholder(R.drawable.baseline_account_circle_avatar)
            .error(android.R.drawable.ic_dialog_alert)
            .transformations(CircleCropTransformation())
            .build()

        this.context.imageLoader.enqueue(request)
    }
}
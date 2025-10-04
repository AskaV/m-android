package com.spp.android.myapplication.presentation.util.extensions

import android.widget.ImageView
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.spp.android.myapplication.R
import com.squareup.picasso.Picasso

var currentImageLoader: ImageLoaderType = ImageLoaderType.COIL

val ImageView.avatarSize: Int
    get() = context.resources.getDimensionPixelSize(R.dimen.profile_avatar_size)

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
        .override(avatarSize, avatarSize)
        .into(this)
}

fun ImageView.loadWithPicasso(url: String?) {
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.baseline_account_circle_avatar)
        .error(android.R.drawable.ic_dialog_alert)
        .transform(CircleTransform())
        .resize(avatarSize, avatarSize)
        .into(this)
}

private fun ImageView.loadWithCoil(url: String?) {
    val request = ImageRequest.Builder(this.context)
        .data(url)
        .target(this)
        .placeholder(R.drawable.baseline_account_circle_avatar)
        .error(android.R.drawable.ic_dialog_alert)
        .transformations(CircleCropTransformation())
        .size(avatarSize, avatarSize)
        .build()

    this.context.imageLoader.enqueue(request)
}

fun randomAvatarUrl(sizePx: Int): String {
    val id = (1..70).random()
    return "https://i.pravatar.cc/$sizePx?img=$id"
}
package com.spp.android.myapplication.screens.util.extensions

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.spp.android.myapplication.R
import com.spp.android.myapplication.xmlscreens.util.extensions.CircleTransform

import com.squareup.picasso.Picasso

var currentImageLoader: ImageLoaderType = ImageLoaderType.FRESCO

enum class ImageLoaderType {
    COIL, GLIDE, PICASSO, FRESCO
}

@Composable
fun LoadAvatarComposable(
    url: String?,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        LoadWithCoil(null, modifier)
        return
    }

    when (currentImageLoader) {
        ImageLoaderType.COIL -> LoadWithCoil(url, modifier)
        ImageLoaderType.GLIDE -> LoadWithGlide(url, modifier)
        ImageLoaderType.PICASSO -> LoadWithPicasso(url, modifier)
        ImageLoaderType.FRESCO -> LoadWithFresco(url, modifier)
    }
}

@Composable
fun LoadWithCoil(
    url: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(url)
                .placeholder(R.drawable.baseline_account_circle_avatar)
                .error(android.R.drawable.ic_dialog_alert)
                .transformations(CircleCropTransformation())
                .build()
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun LoadWithGlide(
    url: String?,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = {
            ImageView(it).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(it)
                    .load(url)
                    .placeholder(R.drawable.baseline_account_circle_avatar)
                    .error(android.R.drawable.ic_dialog_alert)
                    .circleCrop()
                    .into(this)
            }
        }
    )
}

@Composable
fun LoadWithPicasso(
    url: String?,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = {
            ImageView(it).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.baseline_account_circle_avatar)
                    .error(android.R.drawable.ic_dialog_alert)
                    .transform(CircleTransform())
                    .into(this)
            }
        }
    )
}

@Composable
fun LoadWithFresco(
    url: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            SimpleDraweeView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                hierarchy = GenericDraweeHierarchyBuilder(context.resources)
                    .setRoundingParams(RoundingParams.asCircle())
                    .build()

                setImageURI(url)
            }
        },
        modifier = modifier
    )
}
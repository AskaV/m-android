package com.spp.android.myapplication.xmlscreens.util.extensions

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.AttrRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.OpenGalleryPageBinding
import com.squareup.picasso.Picasso

object PhotoChooser {

    enum class Mode { SIGN_UP, EDIT_PROFILE }

    class Portal internal constructor(
        internal val perm: ActivityResultLauncher<String>,
        internal val pick: ActivityResultLauncher<String>,
        internal val camera: ActivityResultLauncher<Void?>
    )

    fun attach(
        caller: ActivityResultCaller,
        onPickedFromGallery: (Uri?) -> Unit,
        onCameraShot: (Bitmap?) -> Unit,
        onPermissionGranted: () -> Unit
    ): Portal {
        val perm = caller.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) onPermissionGranted()
        }

        val pick = caller.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri -> onPickedFromGallery(uri) }

        val camera = caller.registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bmp -> onCameraShot(bmp) }

        return Portal(perm, pick, camera)
    }

    private fun requiredReadImagesPermission(): String =
        if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE

    private fun hasReadImagesPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, requiredReadImagesPermission()) ==
                PackageManager.PERMISSION_GRANTED

    fun ensurePermissionAndShow(
        activity: Activity,
        portal: Portal,
        inflater: LayoutInflater,
        mode: Mode,
        onDeletePhoto: () -> Unit
    ) {
        showDialog(
            context = activity,
            inflater = inflater,
            mode = mode,
            onOpenGallery = { portal.pick.launch("image/*") },
            onDeletePhoto = onDeletePhoto,
            onTakePhoto = { portal.camera.launch(null) }
        )

        if (!hasReadImagesPermission(activity)) {
            portal.perm.launch(requiredReadImagesPermission())
        }
    }

    private fun showDialog(
        context: Context,
        inflater: LayoutInflater,
        mode: Mode,
        onOpenGallery: () -> Unit,
        onDeletePhoto: () -> Unit,
        onTakePhoto: () -> Unit
    ): AlertDialog {
        val binding = OpenGalleryPageBinding.inflate(inflater, null, false)
        val view = binding.root

        when (mode) {
            Mode.SIGN_UP -> {
                binding.photoCard.cardElevation =
                    context.resources.getDimension(R.dimen.spacer_medium)
                binding.photoCard.strokeWidth = 0
            }

            Mode.EDIT_PROFILE -> {
                binding.photoCard.cardElevation =
                    context.resources.getDimension(R.dimen.spacer_small)
                binding.photoCard.strokeWidth =
                    context.resources.getDimensionPixelSize(R.dimen.border_size)
            }
        }

        val colorPrimary = colorFromAttr(context, android.R.attr.textColorPrimary)
        val colorSecondary = colorFromAttr(context, android.R.attr.textColorSecondary)

        when (mode) {
            Mode.SIGN_UP -> {
                binding.actionOpenGallery.setTextColor(colorPrimary)
                binding.actionCancel.setTextColor(colorSecondary)
                binding.actionDelete.visibility = View.GONE
                binding.actionDelete.isEnabled = false
            }

            Mode.EDIT_PROFILE -> {
                binding.actionOpenGallery.setTextColor(colorPrimary)
                binding.actionDelete.setTextColor(colorSecondary)
                binding.actionCancel.setTextColor(colorPrimary)
                binding.actionDelete.visibility = View.VISIBLE
                binding.actionDelete.isEnabled = true
            }
        }

        loadRecentThumbsIfPermitted(context, listOf(binding.thumb1, binding.thumb2, binding.thumb3))

        val raw = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(view)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setDimAmount(0.6f)
                attributes = attributes.apply {
                    width = (context.resources.displayMetrics.widthPixels * 0.88f).toInt()
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                }
            }
        }

        binding.icCamera.setOnClickListener { raw.dismiss(); onTakePhoto() }
        val open = { raw.dismiss(); onOpenGallery() }
        binding.thumb1.setOnClickListener { open() }
        binding.thumb2.setOnClickListener { open() }
        binding.thumb3.setOnClickListener { open() }
        binding.actionOpenGallery.setOnClickListener { open() }
        binding.actionDelete.setOnClickListener { onDeletePhoto(); raw.dismiss() }
        binding.actionCancel.setOnClickListener { raw.dismiss() }

        raw.show()
        return AlertDialog.Builder(context).create()
            .also { it.setOnDismissListener { raw.dismiss() } }
    }


    private fun colorFromAttr(context: Context, @AttrRes attr: Int): Int {
        val tv = TypedValue()
        val ok = context.theme.resolveAttribute(attr, tv, true)
        return if (ok && tv.resourceId != 0)
            ContextCompat.getColor(context, tv.resourceId)
        else
            tv.data
    }

    private fun loadRecentThumbsIfPermitted(
        context: Context,
        targets: List<ImageView?>,
        limit: Int = 3
    ) {
        if (!hasReadImagesPermission(context)) return
        val uris = queryRecentImageUris(context, limit)
        uris.forEachIndexed { i, uri ->
            targets.getOrNull(i)?.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageURI(uri)
            }
        }
    }

    private fun queryRecentImageUris(context: Context, limit: Int): List<Uri> {
        val out = mutableListOf<Uri>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val resolver: ContentResolver = context.contentResolver
        val cursor = if (Build.VERSION.SDK_INT >= 30) {
            val args = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Images.ImageColumns.DATE_ADDED)
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }
            resolver.query(collection, projection, args, null)
        } else {
            resolver.query(
                collection,
                projection,
                null,
                null,
                "${MediaStore.Images.ImageColumns.DATE_ADDED} DESC"
            )
        }

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var c = 0
            while (it.moveToNext() && c < limit) {
                val id = it.getLong(idCol)
                out += Uri.withAppendedPath(collection, id.toString())
                c++
            }
        }
        return out
    }

    fun applyAvatarFromUri(
        context: Context,
        target: ImageView,
        uri: Uri,
        sizeRes: Int = R.dimen.profile_avatar_size
    ) {
        val size = context.resources.getDimensionPixelSize(sizeRes)
        Picasso.get()
            .load(uri)
            .resize(size, size)
            .centerCrop()
            .transform(CircleTransform())
            .into(target)
    }

    fun applyAvatarFromBitmap(
        context: Context,
        target: ImageView,
        source: Bitmap,
        sizeRes: Int = R.dimen.profile_avatar_size
    ) {
        val size = context.resources.getDimensionPixelSize(sizeRes)
        val circ = CircleTransform().transform(source)
        val scaled = if (circ.width != size || circ.height != size)
            Bitmap.createScaledBitmap(circ, size, size, true) else circ
        target.scaleType = ImageView.ScaleType.CENTER_CROP
        target.setImageBitmap(scaled)
    }

}

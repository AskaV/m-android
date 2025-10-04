package com.spp.android.myapplication.presentation.xml.activity

import android.net.Uri
import android.os.Bundle
import com.spp.android.myapplication.R
import com.spp.android.myapplication.databinding.SignUpExtendedPageBinding
import com.spp.android.myapplication.presentation.util.extensions.PhotoChooser
import com.spp.android.myapplication.presentation.util.extensions.ValidationUtils
import com.spp.android.myapplication.presentation.util.extensions.loadAvatar
import com.spp.android.myapplication.presentation.util.extensions.randomAvatarUrl

class SignUpExtendedActivityXml : BaseActivity() {

    private lateinit var binding: SignUpExtendedPageBinding
    private var selectedPhotoUri: Uri? = null

    private lateinit var photoPortal: PhotoChooser.Portal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SignUpExtendedPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email").orEmpty()
        val pass = intent.getStringExtra("password").orEmpty()

        val fields = binding.signUpExtendedFields
        val nameEt = fields.userNameField
        val phoneEt = fields.mobilePhoneField

        photoPortal = PhotoChooser.attach(
            caller = this,
            onPickedFromGallery = { uri ->
                uri?.let {
                    selectedPhotoUri = it
                    PhotoChooser.applyAvatarFromUri(this, binding.avatarImage, it)
                }
            },
            onCameraShot = { bmp ->
                bmp?.let {
                    selectedPhotoUri = null
                    PhotoChooser.applyAvatarFromBitmap(this, binding.avatarImage, it)
                }
            },
            onPermissionGranted = {
                PhotoChooser.ensurePermissionAndShow(
                    activity = this,
                    portal = photoPortal,
                    inflater = layoutInflater,
                    mode = PhotoChooser.Mode.SIGN_UP
                ) {
                    selectedPhotoUri = null
                    binding.avatarImage.setImageResource(R.drawable.baseline_account_circle_avatar)
                }
            }
        )

        val openMenu: () -> Unit = {
            PhotoChooser.ensurePermissionAndShow(
                activity = this,
                portal = photoPortal,
                inflater = layoutInflater,
                mode = PhotoChooser.Mode.SIGN_UP,
                onDeletePhoto = {
                    selectedPhotoUri = null
                    binding.avatarImage.setImageResource(R.drawable.baseline_account_circle_avatar)
                }
            )
        }

        binding.addPhotoBtn.setOnClickListener { openMenu() }
        binding.addPhotoBtn.setOnClickListener {
            val sizePx = resources.getDimensionPixelSize(R.dimen.profile_avatar_size)
            selectedPhotoUri = null
            binding.avatarImage.loadAvatar(randomAvatarUrl(sizePx))
        }

        binding.cancelButton.setOnClickListener { finish() }

        binding.forwardButton.setOnClickListener {
            val allValid = ValidationUtils.validateNameAndPhone(
                context = this,
                nameField = fields.userNameField,
                phoneField = fields.mobilePhoneField,
                nameErrorView = fields.userNameErrorText,
                phoneErrorView = fields.phoneErrorText
            )
            if (!allValid) return@setOnClickListener

            val userName = nameEt.text?.toString()?.trim().orEmpty()
            val phoneDigits = ValidationUtils.normalizePhone(phoneEt.text?.toString().orEmpty())

            // TODO: next add POST
        }
    }
}

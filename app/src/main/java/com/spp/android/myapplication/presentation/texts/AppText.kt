package com.spp.android.myapplication.presentation.texts

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.spp.android.myapplication.R

data class TextKey(@StringRes val res: Int) {
    @Composable
    fun text(): String = stringResource(res)
    fun text(ctx: Context): String = ctx.getString(res)
}

@Composable
fun @receiver:StringRes Int.t(vararg args: Any): String = stringResource(this, *args)
fun Context.t(@StringRes id: Int, vararg args: Any): String = getString(id, *args)

class S(@StringRes val id: Int, vararg val args: Any)


object AppText {

    object Login {
        val EMAIL_LABEL = TextKey(R.string.email)
        val PASSWORD_LABEL = TextKey(R.string.password)
        val TITLE = TextKey(R.string.hello)
        val SUBTITLE = TextKey(R.string.enter_email_password)

        val EMAIL_ERROR = TextKey(R.string.email_error_text)
        val PASSWORD_ERROR_TEMPLATE = R.string.password_error_text

        @Composable
        fun passwordMin(len: Int): String = stringResource(PASSWORD_ERROR_TEMPLATE, len)
        fun passwordMin(ctx: Context, len: Int): String =
            ctx.getString(PASSWORD_ERROR_TEMPLATE, len)

        val REMEMBER_ME = TextKey(R.string.remember_me)
        val FORGOT_PASSWORD = TextKey(R.string.forgot_password)
        val LOGIN = TextKey(R.string.login)
        val DONT_HAVE_ACCOUNT = TextKey(R.string.dont_have_account)
        val SIGN_UP = TextKey(R.string.sign_up)
    }

    object SignUp {
        val TITLE = TextKey(R.string.signup_title)
        val SUBTITLE = TextKey(R.string.signup_subtitle)
        val GOOGLE = TextKey(R.string.signup_google)
        val OR = TextKey(R.string.signup_or)
        val REGISTER = TextKey(R.string.signup_register)
        val TERMS = TextKey(R.string.signup_terms)
        val HAVE_ACCOUNT = TextKey(R.string.signup_have_account)
        val SIGN_IN = TextKey(R.string.signup_sign_in)

        val USERNAME_LABEL = TextKey(R.string.edit_profile_username_label)
        val PHONE_LABEL = TextKey(R.string.edit_profile_phone_label)

        val USERNAME_ERROR = S(R.string.user_name_error, Integers.USERNAME_MIN_LENGTH)
        val PHONE_ERROR = S(R.string.phone_length_error, Integers.PHONE_MIN_LENGTH)
    }

    object Profile {
        val SETTINGS = TextKey(R.string.settings)
        val EDIT = TextKey(R.string.edit_profile)
        val VIEW_CONTACTS = TextKey(R.string.view_contacts)
        val TAB_PROFILE = TextKey(R.string.tab_profile)
    }

    object GalleryStrings {
        const val OPEN_GALLERY = "Open gallery"
        const val DELETE_PHOTO = "Delete current photo"
        const val CANCEL = "Cancel"
    }

    object Contacts {
        val TITLE = TextKey(R.string.contacts_text)
        val ADD = TextKey(R.string.add_contacts_text)
        val TAB_CONTACTS = TextKey(R.string.tab_contacts)
        val DELETE_TOAST = TextKey(R.string.deleted_contact_toast_text)
        val UNDO = TextKey(R.string.return_contact_toast_text)
        val SAVE = TextKey(R.string.add_contacts_save_text)
        val CANCEL = TextKey(R.string.add_contacts_cancel_text)
        val NAME = TextKey(R.string.add_contacts_name)
        val POSITION = TextKey(R.string.add_contacts_position)
    }

    object Common {
        val BACK = TextKey(R.string.back)
    }

    object EditProfile {
        val TITLE = TextKey(R.string.edit_profile_title)
        val SAVE = TextKey(R.string.edit_profile_save)
        val USERNAME_LABEL = TextKey(R.string.edit_profile_username_label)
        val USERNAME = TextKey(R.string.edit_profile_username_placeholder)
        val CAREER_LABEL = TextKey(R.string.edit_profile_career_label)
        val CAREER = TextKey(R.string.edit_profile_career_placeholder)
        val PHONE_LABEL = TextKey(R.string.edit_profile_phone_label)
        val PHONE = TextKey(R.string.edit_profile_phone_placeholder)
        val BIRTHDATE_LABEL = TextKey(R.string.edit_profile_birthdate_label)
        val BIRTHDATE = TextKey(R.string.edit_profile_birthdate_placeholder)
        val ADDRESS_LABEL = TextKey(R.string.edit_profile_address_label)
        val ADDRESS = TextKey(R.string.edit_profile_address_placeholder)
    }

    object Preview {
        val FILLED_BTN_TEXT = TextKey(R.string.filled_button_text)
        val OUTLINED_BTN_TEXT = TextKey(R.string.outlined_button_text)

        const val EMAIL = "example@email.com"
        const val WRONG_EMAIL = "ex"
        const val PASSWORD = "qwerty123"
        const val WRONG_PASSWORD = "123"
        const val USERNAME = "Lucile Alvarado"
        const val WRONG_USERNAME = "Lu"
        const val PHONE = "(264) 654 - 3..."
        const val WRONG_PHONE = "12345"
        const val DOTS = "••••••••"
    }

    object Integers {
        const val PASSWORD_MIN_LENGTH = 8
        const val USERNAME_MIN_LENGTH = 3
        const val PHONE_MIN_LENGTH = 10
    }

    object Label {
        const val EMAIL = "E-mail"
        const val PASSWORD = "Password"
        const val USERNAME = "User name"
        const val PHONE = "Mobile phone"
    }

    object Error {
        const val PASSWORD = "Your password must include a minimum of 8 characters."
        const val USERNAME = "User name must contain at least 3 letters"
        const val PHONE = "Phone must be at least 10 digits long"
    }

    object ExtendedRegister {
        const val TITLE = "Your profile data"
        const val SUBTITLE = "Fill out the profile and go to the application!"

        const val BUTTON = "Cancel"
        const val BUTTON2 = "Forward"
    }

    object MyProfile {
        const val NAME = "Lucile Alvarado"
        const val CAREER = "Career"
        const val ADDRESS = "Home address"
        const val LOGOUT = "Log out"
        const val PROFILE_FILL_HINT = "Go to settings and fill out the profile"
        const val VIEW_CONTACTS = "View my contacts"
        const val EDIT_PROFILE = "Edit profile"
        const val SETTINGS = "Settings"
        const val PROFILE = "Profile"
    }

    object MyProfileDetailed {
        const val NAME = "Lucile Alvarado"
        const val CAREER = "Graphic designer"
        const val ADDRESS = "5295 Gaylord Walks Apk. 110"
    }

    object ContactProfile {
        const val MESSAGE_TEXT = "Message"
        const val ADD_TO_MY_CONTACTS = "Add to my contacts"
    }

}
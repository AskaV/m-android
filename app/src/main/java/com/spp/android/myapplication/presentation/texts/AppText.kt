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

@Composable
fun S.text(): String = stringResource(id, *args)
fun S.text(ctx: Context): String = ctx.getString(id, *args)


object AppText {

    object Login {
        val EMAIL_LABEL = TextKey(R.string.email)
        val PASSWORD_LABEL = TextKey(R.string.password)
        val TITLE = TextKey(R.string.hello)
        val SUBTITLE = TextKey(R.string.enter_email_password)
        val EMAIL_ERROR = TextKey(R.string.email_error_text)
        val BLANK_EMAIL_ERROR = TextKey(R.string.blank_email_error_text)

        val PASSWORD_ERROR_TEMPLATE = R.string.password_error_text
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
    }

    object HomeTabs {
        val PROFILE = TextKey(R.string.profile_text)
        val CONTACTS = TextKey(R.string.contacts_text)
    }

    object MyProfile {
        val NAME = TextKey(R.string.user_name)
        val CAREER = TextKey(R.string.edit_profile_career_label)
        val ADDRESS = TextKey(R.string.edit_profile_address_label)
        val LOGOUT = TextKey(R.string.log_out)
        val PROFILE_FILL_HINT = TextKey(R.string.profile_fill_hint)
        val VIEW_CONTACTS = TextKey(R.string.view_contacts)
        val EDIT_PROFILE = TextKey(R.string.edit_profile)
        val SETTINGS = TextKey(R.string.settings)
        val PROFILE = TextKey(R.string.profile_text)
    }

    object ContactProfile {
        val MESSAGE_TEXT = TextKey(R.string.message_text)
        val ADD_TO_MY_CONTACTS = TextKey(R.string.add_contacts_text)
    }

    object GalleryStrings {
        val OPEN_GALLERY = TextKey(R.string.open_gallery)
        val DELETE_PHOTO = TextKey(R.string.delete_photo)
        val CANCEL = TextKey(R.string.add_contacts_cancel_text)
    }

    object Contacts {
        val TITLE = TextKey(R.string.contacts_text)
        val ADD = TextKey(R.string.add_contacts_text)
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

    object Error {
        val PASSWORD_ERROR = S(R.string.password_error_text, Integers.PASSWORD_MIN_LENGTH)
        val USERNAME_ERROR = S(R.string.user_name_error, Integers.USERNAME_MIN_LENGTH)
        val PHONE_ERROR = S(R.string.phone_length_error, Integers.PHONE_MIN_LENGTH)
    }

    object Label {
        const val EMAIL = "E-mail"
        const val PASSWORD = "Password"
        const val USERNAME = "User name"
        const val PHONE = "Mobile phone"
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

    object ExtendedRegister {
        val TITLE = TextKey(R.string.title)
        val SUBTITLE = TextKey(R.string.subtitle)
        val BUTTON = TextKey(R.string.add_contacts_cancel_text)
        val BUTTON2 = TextKey(R.string.button2)
        val NO_RESULTS_TITLE = TextKey(R.string.no_results_found_title)
        val NO_RESULTS_SUBTITLE = TextKey(R.string.no_results_found_subtitle)
    }

    object MyProfileDetailed {
        val NAME = TextKey(R.string.user_name)
        val CAREER = TextKey(R.string.user_profession)
        val ADDRESS = TextKey(R.string.user_address)
    }

    object OtherInfo {
        val UNKNOWN_ERROR = TextKey(R.string.unknown_error)
        val LOGIN_FAILED = TextKey(R.string.login_failed)
        val LOGOUT_FAILED = TextKey(R.string.logout_failed) //
        val CONTACTS_REMOVED = TextKey(R.string.contact_removed)
        val CONTACTS_LOAD_FAILED = TextKey(R.string.failed_to_load_contacts)
        val FAILED_TO_ADD_CONTACT = TextKey(R.string.failed_to_add_contact)
        val CONTACT_ADDED = TextKey(R.string.contact_added)
        val FAILED_PROFILE_SAVE = TextKey(R.string.failed_to_save_profile)
        val FAILED_TO_LOAD_PROFILE = TextKey(R.string.failed_to_load_profile)
    }
}
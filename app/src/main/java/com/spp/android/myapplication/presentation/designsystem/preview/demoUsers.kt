package com.spp.android.myapplication.presentation.designsystem.preview

import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi

fun demoUsers(): List<ContactUi> = listOf(
    ContactUi(1,  ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1, avatarUrl = null),
    ContactUi(2,  ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2, avatarUrl = null),
    ContactUi(3,  ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3, avatarUrl = null),
    ContactUi(4,  ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4, avatarUrl = null),
    ContactUi(5,  ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5, avatarUrl = null),
    ContactUi(6,  ContactPreviewText.Preview.NAME6, ContactPreviewText.Preview.SUBTITLE6, avatarUrl = null),

    )

object ContactPreviewText {

    object Preview {
        const val NAME1 = "Ava Smith"
        const val SUBTITLE1 = "Photograph"

        const val NAME2 = "Jessie Brown"
        const val SUBTITLE2 = "Actress"

        const val NAME3 = "Jackie Taylor"
        const val SUBTITLE3 = "Financier"

        const val NAME4 = "Jenny Walker"
        const val SUBTITLE4 = "Make-up artist"

        const val NAME5 = "Freddy Harris"
        const val SUBTITLE5 = "Secretary"

        const val NAME6 = "Annie King"
        const val SUBTITLE6 = "Nurse"
    }
}
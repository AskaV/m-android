package com.spp.android.myapplication.presentation.designsystem.preview

import com.spp.android.myapplication.domain.model.Contact

fun demoUsers(): List<Contact> =
    listOf(
        Contact(1, ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1),
        Contact(2, ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2),
        Contact(3, ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3),
        Contact(4, ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4),
        Contact(5, ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5),
        Contact(6, ContactPreviewText.Preview.NAME6, ContactPreviewText.Preview.SUBTITLE6),
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

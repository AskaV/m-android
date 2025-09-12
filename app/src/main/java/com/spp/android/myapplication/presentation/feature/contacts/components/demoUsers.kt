package com.spp.android.myapplication.presentation.feature.contacts.components

import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.ContactPreviewText

fun demoUsers(): List<ContactUi> = listOf(
    ContactUi("1",  ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1, avatarUrl = null),
    ContactUi("2",  ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2, avatarUrl = null),
    ContactUi("3",  ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3, avatarUrl = null),
    ContactUi("4",  ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4, avatarUrl = null),
    ContactUi("5",  ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5, avatarUrl = null),
)
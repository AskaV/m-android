package com.spp.android.myapplication.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.spp.android.myapplication.R

val OpenSans =
    FontFamily(
        Font(R.font.open_sans_regular, FontWeight.Normal),
        Font(R.font.open_sans_semibold, FontWeight.SemiBold),
    )

val Typography =
    Typography(
        // H1
        bodyLarge =
            TextStyle(
                fontFamily = OpenSans,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 31.2.sp,
                letterSpacing = 0.5.sp,
            ),
        // H2
        titleLarge =
            TextStyle(
                fontFamily = OpenSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 23.4.sp,
                letterSpacing = 0.sp,
            ),
        // H3
        titleMedium =
            TextStyle(
                fontFamily = OpenSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 18.2.sp,
                letterSpacing = 0.sp,
            ),
        // H4
        titleSmall =
            TextStyle(
                fontFamily = OpenSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 15.6.sp,
                letterSpacing = 0.sp,
            ),
    )

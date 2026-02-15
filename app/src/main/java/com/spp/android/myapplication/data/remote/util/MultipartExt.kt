package com.spp.android.myapplication.data.remote.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val TEXT_PLAIN = "text/plain".toMediaType()

fun String.toPart(): RequestBody = toRequestBody(TEXT_PLAIN)

fun File.toImagePart(partName: String = "image"): MultipartBody.Part {
    val body = asRequestBody("image/*".toMediaType())
    return MultipartBody.Part.createFormData(partName, name, body)
}

package com.spp.android.myapplication.domain.storage

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AvatarStorage
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        suspend fun saveAvatarFromUri(uriString: String): String =
            withContext(Dispatchers.IO) {
                val uri = uriString.toUri()
                val input =
                    context.contentResolver.openInputStream(uri)
                        ?: error("Can't open input stream for uri=$uri")

                val dir = File(context.filesDir, "avatars").apply { mkdirs() }
                val outFile = File(dir, "avatar_${System.currentTimeMillis()}.jpg")

                input.use { ins ->
                    outFile.outputStream().use { outs ->
                        ins.copyTo(outs)
                    }
                }

                outFile.absolutePath
            }

        suspend fun deleteAvatar(path: String?) =
            withContext(Dispatchers.IO) {
                if (path.isNullOrBlank()) return@withContext
                runCatching { File(path).delete() }
            }
    }

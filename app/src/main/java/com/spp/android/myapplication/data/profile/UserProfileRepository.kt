package com.spp.android.myapplication.data.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.spp.android.myapplication.data.contacts.AvatarProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class UserProfileRepository @Inject constructor(
    private val store: DataStore<Preferences>,
    private val avatarProvider: AvatarProvider
) {
    private object K {
        val NAME = stringPreferencesKey("p_name")
        val CAREER = stringPreferencesKey("p_career")
        val PHONE = stringPreferencesKey("p_phone")
        val EMAIL = stringPreferencesKey("p_email")
        val ADDRESS = stringPreferencesKey("p_address")
        val AVATAR = stringPreferencesKey("p_avatar")
        val SEED = intPreferencesKey("p_seed")
        val PASS_SAVED = booleanPreferencesKey("p_is_password_saved")
    }

    val profile: Flow<UserProfile> = store.data.map { p ->
        val seed = p[K.SEED] ?: Random.nextInt()
        val avatar = p[K.AVATAR] ?: avatarProvider.forId(seed, null)
        UserProfile(
            name = p[K.NAME].orEmpty(),
            career = p[K.CAREER].orEmpty(),
            phone = p[K.PHONE].orEmpty(),
            email = p[K.EMAIL].orEmpty(),
            address = p[K.ADDRESS].orEmpty(),
            avatarUrl = avatar,
            isPasswordSaved = p[K.PASS_SAVED] ?: false
        )
    }

    suspend fun ensureAvatar() {
        store.updateData { pref ->
            val m = pref.toMutablePreferences()
            if (m[K.AVATAR].isNullOrBlank()) {
                val seed = m[K.SEED] ?: Random.nextInt()
                m[K.SEED] = seed
                m[K.AVATAR] = avatarProvider.forId(seed, null)
            }
            m
        }
    }

    suspend fun save(profile: UserProfile) {
        store.updateData { pref ->
            pref.toMutablePreferences().apply {
                this[K.NAME] = profile.name
                this[K.CAREER] = profile.career
                this[K.PHONE] = profile.phone
                this[K.EMAIL] = profile.email
                this[K.ADDRESS] = profile.address
                profile.avatarUrl?.takeIf { it.isNotBlank() }?.let { this[K.AVATAR] = it }
                this[K.PASS_SAVED] = profile.isPasswordSaved
            }
        }
    }
}
package dev.mjanusz.recruitmentapp.ui.common

import android.app.UiModeManager
import android.app.UiModeManager.MODE_NIGHT_AUTO
import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import android.os.Build
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import dev.mjanusz.recruitmentapp.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiModeHelper @Inject constructor(
    private val uiModeManager: UiModeManager
) {

    fun setUiMode(uiMode: UiMode) {
        val mode = when(uiMode) {
            UiMode.DAY -> MODE_NIGHT_NO
            UiMode.NIGHT -> MODE_NIGHT_YES
            UiMode.SYSTEM -> MODE_NIGHT_AUTO
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiModeManager.setApplicationNightMode(mode)
        } else {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    enum class UiMode(@StringRes val stringRes: Int) {
        DAY(R.string.mode_light),
        NIGHT(R.string.mode_dark),
        SYSTEM(R.string.mode_system)
    }
}
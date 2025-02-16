package tech.ericwathome.currencybuddy.error

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import tech.ericwathome.core.presentation.ui.BuildConfig
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    private val crashlytics = Firebase.crashlytics
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        if (BuildConfig.DEBUG) {
            crashlytics.isCrashlyticsCollectionEnabled = false
            return
        }

        crashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)

        if (tag != null) {
            crashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, tag)
        }

        crashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)

        if (t == null) {
            crashlytics.recordException(Exception(message))
        } else {
            crashlytics.recordException(t)
        }
    }

    companion object {
        const val CRASHLYTICS_KEY_PRIORITY = "priority"
        const val CRASHLYTICS_KEY_TAG = "tag"
        const val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}
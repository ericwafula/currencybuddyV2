package tech.ericwathome.currencybuddy.error

import android.content.Context
import android.os.Build
import android.os.Process
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import tech.ericwathome.currencybuddy.BuildConfig
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Calendar
import kotlin.system.exitProcess

class CrashHandler : Thread.UncaughtExceptionHandler {
    private val newLine = "\n"
    private var errorMessage: String? = null
    private var softwareInfo: String? = null
    private var dateInfo: String? = null

    override fun uncaughtException(t: Thread, e: Throwable) {
        val stackTrace = StringWriter()
        e.printStackTrace(PrintWriter(stackTrace))

        errorMessage = buildString {
            append(stackTrace.toString())
        }

        softwareInfo = buildString {
            append("SDK: ")
            append(Build.VERSION.SDK_INT)
            append(newLine)
            append("Release: ")
            append(Build.VERSION.RELEASE)
            append(newLine)
            append("incremental: ")
            append(Build.VERSION.INCREMENTAL)
            append(newLine)
        }

        dateInfo = buildString {
            append(Calendar.getInstance().time)
            append(newLine)
        }

        val message = "Error:\n" +
                "$errorMessage\n\n" +
                "Software:\n" +
                "$softwareInfo\n\n" +
                "Date:\n" +
                "$dateInfo"

        Timber.e(message)

        Process.killProcess(Process.myPid())
        exitProcess(2)
    }
}
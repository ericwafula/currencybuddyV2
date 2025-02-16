package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import java.io.File
import java.util.Properties

abstract class BaseBumperTask : DefaultTask() {
    @OutputFile
    val versionsFile: File = project.rootProject.file("version.properties")
    @Input
    val properties = Properties().apply {
        if (versionsFile.exists()) {
            load(versionsFile.inputStream())
        } else {
            versionsFile.createNewFile()
        }
    }
    @Input
    val getVersionNameList = properties["versionName"].toString().split(".")
    @Input
    private val getVersionCode = properties["versionCode"].toString()

    fun incrementVersionCode(): String = (getVersionCode.toInt() + 1).toString()

    fun save(versionName: String, versionCode: String) {
        properties["versionName"] = versionName
        properties["versionCode"] = versionCode

        properties.store(versionsFile.outputStream(), null)

        project.exec {
            commandLine("git", "add", versionsFile.absolutePath)
        }

        project.exec {
            commandLine("git", "commit", "-m", "Bump version to $versionName ($versionCode)")
        }
    }
}
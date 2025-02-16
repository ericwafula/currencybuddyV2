package tasks

import org.gradle.api.tasks.TaskAction

abstract class BumperVersionMinorTask : BaseBumperTask() {

    @TaskAction
    fun bumpVersionMinor() {
        val versionName =
            getVersionNameList[0] + "." + (getVersionNameList[1].toInt() + 1) + "." + 0 + "." + 0
        val versionCode = incrementVersionCode()

        save(versionName, versionCode)
    }

}
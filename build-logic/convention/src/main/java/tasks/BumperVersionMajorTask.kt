package tasks

import org.gradle.api.tasks.TaskAction

abstract class BumperVersionMajorTask : BaseBumperTask() {

    @TaskAction
    fun bumperVersionMajor() {
        val versionName =
            (getVersionNameList[0].toInt() + 1).toString() + "." + 0 + "." + 0 + "." + 0
        val versionCode = incrementVersionCode()

        save(versionName, versionCode)
    }

}
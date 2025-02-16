package tasks

import org.gradle.api.tasks.TaskAction

abstract class BumperVersionPatchTask : BaseBumperTask() {

    @TaskAction
    fun bumperVersionPatch() {
        val versionName =
            getVersionNameList[0] + "." + getVersionNameList[1] + "." + (getVersionNameList[2].toInt() + 1) + "." + 0
        val versionCode = incrementVersionCode()

        save(versionName, versionCode)
    }

}
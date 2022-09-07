package com.example.plugin01

import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.ProcessApplicationManifest
import groovy.xml.XmlParser
import org.gradle.api.Action
import org.gradle.api.Project
import com.example.plugin01.O
import com.example.plugin01.extensions.CusExported
import com.example.plugin01.task.AddExportMainManifestTask

class XMLUtils {

    fun readXml(target: Project, variantNames: MutableList<String>) {
        variantNames.forEach {
            val absName = String.format("process%sMainManifest", it.capitalize())
            //一个project下面有Task,Task下面有Action
            val manifestTask = target.tasks.getByName(absName) as ProcessApplicationManifest
            //自定义task
            val taskKey = "$it+${O.CUS_TASK_NAME}"
            val exportedTask = target.tasks.create(taskKey, AddExportMainManifestTask::class.java)
            val ext = target.properties[O.lyfSdk] as CusExported
            //给task添加ext信息
            exportedTask.extArg = ext
            exportedTask.mainManifest = manifestTask.mainManifest.get()
            exportedTask.manifests = manifestTask.getManifests().files
            //dependsOn属性，表示当前Task依赖 xx Task，当前Task执行的时候需要先执行 xx Task
            //exportedTask 先执行。再执行manifestTask。
            manifestTask.dependsOn(exportedTask)
        }
    }


}
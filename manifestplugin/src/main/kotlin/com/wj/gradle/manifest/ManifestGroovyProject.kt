package com.wj.gradle.manifest

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.tasks.ProcessApplicationManifest
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

class ManifestGroovyProject : Plugin<Project> {

    private val variantNames = ArrayList<String>()

    companion object {
        private const val EXPORTED_EXT = "exported"
        private const val TASK_NAME = "ManifestExportedTask"
    }

    @Override
    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin(AppPlugin::class.java)) return
        println("-----apply------${project.name}")
        //Extension其实可以理解成java中的java bean, 它的作用也是类似的，即获取输入数据，然后在插件中使用。
        project.extensions.create(EXPORTED_EXT, ExportedExtension::class.java)
        project.task(TASK_NAME)
        val ext = project.properties[EXPORTED_EXT] as ExportedExtension
        readAppModelVariant(project)
        //
        project.afterEvaluate {
            println("-----afterEvaluate------")
            if (ext.logOutPath.isEmpty()) {
                // logOutPath 日志输出目录，默认 app/build/exported/outManifest.md
                ext.logOutPath = it.buildDir.absoluteFile.path
                println("-----afterEvaluate logOutPath------${ext.logOutPath}")
            }
            println("-----afterEvaluate logOutPath------${ext.logOutPath}")
            addMainManifestTask(ext, project)
        }
    }

    private fun readAppModelVariant(project: Project) {
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        //获取Andorid封装的AppExtension拓展，获取变体
        appExtension.variantFilter(
            Action {
                variantNames.add(
                    it.name
                )
                //获取变体名称
                //-----variantFilter name--debug
                //-----variantFilter name--release
                println("-----variantFilter name--${it.name}")
            }
        )
    }

    /**
     * 添加task到processxxxMainManifest之后
     * 如 processDebugMainManifest
     * */
    private fun addMainManifestTask(ext: ExportedExtension, p: Project) {
        variantNames.forEach {
            val absName = String.format("process%sMainManifest", it.capitalize())
            println("-----absName name-->${absName}")
            //-----absName name--processDebugMainManifest
            //-----absName name--processReleaseMainManifest
            val t = p.tasks.getByName(absName) as ProcessApplicationManifest
            //创建自定义DefaultTask
            val taskKey = "$it$TASK_NAME"
            val exportedTask = p.tasks.create(taskKey, AddExportMainManifestTask::class.java)
            exportedTask.setExtArg(ext)//设置拓展信息
            exportedTask.setMainManifest(t.mainManifest.get())//mainManifest 文件
            exportedTask.setManifests(t.getManifests().files)//mainManifest s
            t.dependsOn(exportedTask)//task 依赖执行。
        }
    }

/*
 > Task :app:extractDeepLinksDebug UP-TO-DATE
> Task :login_model:extractDeepLinksDebug UP-TO-DATE
> Task :login_model:processDebugManifest UP-TO-DATE
> Task :app:processDebugMainManifest
> Task :app:processDebugManifest
> Task :login_model:compileDebugLibraryResources UP-TO-DATE
> Task :login_model:parseDebugLocalResources UP-TO-DATE
> Task :login_model:generateDebugRFile UP-TO-DATE
> Task :login_model:generateDebugBuildConfig UP-TO-DATE
> Task :login_model:compileDebugKotlin UP-TO-DATE
> Task :login_model:javaPreCompileDebug UP-TO-DATE
> Task :login_model:compileDebugJavaWithJavac UP-TO-DATE
> Task :login_model:bundleLibCompileToJarDebug UP-TO-DATE
> Task :app:javaPreCompileDebug UP-TO-DATE
> Task :app:mergeDebugNativeDebugMetadata NO-SOURCE
> Task :app:mergeDebugShaders UP-TO-DATE
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :login_model:mergeDebugShaders UP-TO-DATE
> Task :login_model:compileDebugShaders NO-SOURCE
> Task :login_model:generateDebugAssets UP-TO-DATE
> Task :login_model:packageDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets UP-TO-DATE
> Task :app:compressDebugAssets UP-TO-DATE
> Task :app:processDebugJavaRes NO-SOURCE
> Task :login_model:processDebugJavaRes NO-SOURCE
> Task :login_model:bundleLibResDebug UP-TO-DATE
> Task :app:checkDebugDuplicateClasses UP-TO-DATE
> Task :app:desugarDebugFileDependencies
*/
}
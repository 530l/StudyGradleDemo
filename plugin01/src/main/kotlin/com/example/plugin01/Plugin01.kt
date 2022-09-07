package com.example.plugin01

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.example.plugin01.O.TAG
import com.example.plugin01.extensions.CusExported
import org.gradle.api.Action

class Plugin01 : Plugin<Project> {

    private val fileUtils = FileUtils()
    private val xmlUtils = XMLUtils()

    private val variantNames = ArrayList<String>()

    companion object {

    }

    //1.初始化，2.配置。3.执行。
    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin(AppPlugin::class.java)) {
            println(TAG + "没有AppPlugin")
            return
        }

        target.extensions.create(O.lyfSdk, CusExported::class.java)//添加自定义拓展ext
        target.task(O.CUS_TASK_NAME)//创建task
        readAppModelVariant(target)
        ///todo 执行，
        target.afterEvaluate {
            //获取自定义拓展ext,拓展存在target.properties中。
            val cusExt = target.properties[O.lyfSdk] as CusExported
            println("$TAG  cusExt $cusExt   logOutPath ${cusExt.logOutPath}")
            xmlUtils.readXml(target,variantNames)
        }
    }


    private fun readAppModelVariant(project: Project) {
        //Variant API介绍
        //关于Variant API，我们首先了解一下什么是Variant?
        //其实Variant就是buildType与Flavor的组合
        //Variant API 是 AGP(Android Gradle Plugin) 中的扩展机制ext，可让您操纵build.gradle中的各种配置。
        // 您还可以通过 Variant API 访问构建期间创建的中间产物和最终产物，例如类文件、合并后的Manifest或 APK/AAB 文件。
        //ExtensionContainer 扩展容器
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        //变体过滤器
        appExtension.variantFilter(Action {
            //@530--->>  variant name debug
            //@530--->>  variant name release
            println(TAG + "  variant name " + it.name)
            variantNames.add(it.name)
        })
    }
}
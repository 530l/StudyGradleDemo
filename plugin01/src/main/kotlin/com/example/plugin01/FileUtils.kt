package com.example.plugin01

import com.example.plugin01.O.TAG
import org.gradle.api.Project
import java.io.File

//        val targetName = target.name
//        val targetParentName = target.parent?.name
//        //@530--->>targetName=app,,,targetParentName=StudyGradleDemo
////        println(TAG + "targetName=${targetName},,,targetParentName=${targetParentName}")
////        fileUtils.readFile(target)
////        fileUtils.writeOut(target)
//        //读取变体,变体名字根据配置来。
class FileUtils {


    fun readFile(target: Project) {
        //创建文件
        val file = File("${target.rootDir}/text.txt")
        //输出文件的每一行
        file.forEachLine {
            println("$TAG  forEachLine  $it")
        }

        //文件内容一次性读出，返回类型为 byte[]
        val bytes = file.readBytes()
        val builder = StringBuilder()
        builder.append(String(bytes))
        println("$TAG  readBytes $builder")
    }

     fun writeOut(target: Project) {
        val wikiFileDir = File("${target.rootDir}/exported")
        if (!wikiFileDir.exists()) wikiFileDir.mkdir()
        val wikiFile = File(wikiFileDir, "outManifestLog.md")
        if (wikiFile.exists()) wikiFile.delete()
        wikiFile.writeText("我写点东西进来吧。。。。")
    }

}
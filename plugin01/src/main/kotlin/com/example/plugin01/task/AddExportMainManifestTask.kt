package com.example.plugin01.task

import com.example.plugin01.extensions.CusExported
import org.gradle.api.DefaultTask
import java.io.File

class AddExportMainManifestTask : DefaultTask() {

     lateinit var extArg: CusExported//扩展。

    // mainManifest
     lateinit var mainManifest: File

    // 第三方aar的Manifest
     lateinit var manifests: Set<File>
}
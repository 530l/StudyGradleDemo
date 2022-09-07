package com.example.plugin01.extensions

//拓展，可以在使用插件的地方，定义，获取信息。
// build.gradle{ CusExported{ xxx.xxx} }
open class CusExported {
    // 日志输出位置,会输出以下内容
    var logOutPath: String = ""
}
# WilsonPanel
一套Android源生快速开发基础库

使用方法：
1、在工程 build.gradle 中添加：
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

2、在app项目build.gradle 中添加引用：
  implementation 'com.github.SeptemberWei:WilsonPanel:1.0'

项目介绍：
1、为方便使用已经引入了butterknife，如需使用请在app的build.gradle中添加如下代码
  annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'

2、已引入：eventbus、retrofit2、okhttp、rxjava2、rxandroid、rxpermissions

3、Activity可以直接继承BaseActivity（com.wilson.panellibrary.base.activity.BaseActivity）并实现 getLayoutResId（）方法及init（）方法，Fragment使用方法类似。

4、Presenter使用方法：自定义Presenter例如（MainPresenter）继承AbstractRxPresenter，具体用法见项目示例。

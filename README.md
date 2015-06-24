# Simple app for Notes 

 + OS                     : Android
 + Build system           : Gradle (1.+)
 + SDK                    : 22
 + Android tested version : 5 (Lolipop)

 ## Dependencies (same as in build.gradle)

    dependencies {
        compile fileTree(include: '*.jar', dir: 'libs')
        compile 'nl.qbusict:cupboard:2.1.1'
        compile 'com.melnykov:floatingactionbutton:1.3.0'
        compile 'com.android.support:appcompat-v7:22.2.0'
        compile project(':lib:SlidingMenu')
    }

# Simple app for Notes 

## Info

 + OS                     : Android
 + Build system           : Gradle (1.+)
 + SDK                    : 22
 + Android tested version : 5 (Lolipop)

## Little hints

 If you know java you`ll find sources without troubles, but if you don`t you can look at :
   + *src/com/struckoff/Notes* here you`ll find some sources (*.java)
   + *res/layout/*             here you`ll find some layout configs (*.xml)
   + *res/values*              here you`ll find some value aliases and styles (*.xml)

## Dependencies (same as in build.gradle)

````gradle
    dependencies {
        compile fileTree(include: '*.jar', dir: 'libs')
        compile 'nl.qbusict:cupboard:2.1.1'
        compile 'com.melnykov:floatingactionbutton:1.3.0'
        compile 'com.android.support:appcompat-v7:22.2.0'
        compile project(':lib:SlidingMenu')
    }
````

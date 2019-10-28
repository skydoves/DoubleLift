# DoubleLift

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=16"><img alt="API" src="https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://travis-ci.com/skydoves/DoubleLift"><img alt="Build Status" src="https://travis-ci.com/skydoves/DoubleLift.svg?branch=master"/></a>
  <a href="https://skydoves.github.io/libraries/doublelift/javadoc/doublelift/com.skydoves.doublelift/index.html"><img alt="Javadoc" src="https://img.shields.io/badge/Javadoc-DoubleLift-yellow"/></a>
</p>

<p align="center">
🦋 Expands and collapses a layout's horizontal and vertical sequentially.<br>
Inspired by "Viewing Labels" from the Trello.
</p>

<p align="center">
<img src="https://user-images.githubusercontent.com/24237865/67579148-dbf95000-f77e-11e9-8e90-20fd64304f66.gif" width="32%"/>
<img src="https://user-images.githubusercontent.com/24237865/67660029-22c69000-f9a1-11e9-9de3-31cfe026bf5a.gif" width="32%"/>
</p>

## Including in your project
[![Download](https://api.bintray.com/packages/devmagician/maven/doublelift/images/download.svg)](https://bintray.com/devmagician/maven/doublelift/_latestVersion)
[![Jitpack](https://jitpack.io/v/skydoves/DoubleLift.svg)](https://jitpack.io/#skydoves/DoubleLift)
### Gradle 
Add below codes to your **root** `build.gradle` file (not your module build.gradle file).
```gradle
allprojects {
    repositories {
        jcenter()
    }
}
```
And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation "com.github.skydoves:doublelift:1.0.1"
}
```

## Usage
Add following XML namespace inside your XML layout file.

```gradle
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### DoubleLiftLayout
Here is a basic example of implementing `DoubleLiftLayout`.

```gradle
<com.skydoves.doublelift.DoubleLiftLayout
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  app:doubleLift_foldedWidth="50dp" // sets the width size when collapsed.
  app:doubleLift_foldedHeight="10dp" // sets the height size when collapsed.
  app:doubleLift_horizontalDuration="400" // sets the duration of horizontal lifting.
  app:doubleLift_verticalDuration="300" // sets the duration of vertical lifting.
  app:doubleLift_cornerRadius="4dp" // sets the corner radius.
  app:doubleLift_autoExpand="false" // expand initially or not.
  app:doubleLift_startOrientation="horizontal"
  app:doubleLift_animation="bounce" // sets the lifting animation when expanding and collapsing
  >

  <TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:text="Feature"
    android:textColor="@color/white_87"
    android:textStyle="bold" />
    
  // something complicated views or layouts
  
</com.skydoves.doublelift.DoubleLiftLayout>
```

### Create using builder class
We can create an instance of `DoubleLiftLayout` using the `DoubleLiftLayout.Builder` class.
```kotlin
val myDoubleLiftLayout = DoubleLiftLayout.Builder(context)
  .setFoldedWidth(200)
  .setFoldedHeight(100)
  .setCornerRadius(6)
  .setLiftHorizontalDuration(400)
  .setLiftVerticalDuration(200)
  .setOnExpandListener { toast("expanded: $it") }
  .build()
```
Or we can create using the kotlin-dsl.
```kotlin
val myDoubleLiftLayout = doubleLiftLayout(this) {
  setFoldedWidth(200)
  setFoldedHeight(100)
  setCornerRadius(6)
  setLiftHorizontalDuration(400)
  setLiftVerticalDuration(200)
  setOnExpandListener { toast("expanded: $it") }
}
```

### Expand and Collapse
We can expand and collapse using the below methods.
```kotlin
doubleLiftLayout.expand() // expand the width and height size sequentially.
doubleLiftLayout.collapse() // collapse the width and height size sequentially.
```

or we can do something after expanded using lambda.

```kotlin
doubleLiftLayout.expand { toast("expanded") }
doubleLiftLayout.collapse { toast("collapsed") }
```

### OnExpandListener
We can listen to the `DoubleLiftLayout` is expanded or collapsed.
```kotlin
doubleLiftLayout.onExpandListener = object : OnExpandListener {
  override fun onExpand(isExpanded: Boolean) {
    toast("Expanded : $it")
  }
}

// or we can listen using a lambda expression.
doubleLiftLayout.setOnExpandListener {
  if (it) {
    toast("expanded")
  } else {
    toast("collapse")
  }
```

### LiftAnimation
We can customize the expanding and collapsing animation.<br>
```kotlin
LiftAnimation.NORMAL
LiftAnimation.Accelerator
LiftAnimation.Bounce
```

NORMAL | Accelerator | Bounce
| :---------------: | :---------------: | :---------------: |
| <img src="https://user-images.githubusercontent.com/24237865/67661136-c749d180-f9a3-11e9-82c4-48975a704412.gif" align="center" width="100%"/> | <img src="https://user-images.githubusercontent.com/24237865/67661132-c749d180-f9a3-11e9-8689-904d60a74e42.gif" align="center" width="100%"/> | <img src="https://user-images.githubusercontent.com/24237865/67661134-c749d180-f9a3-11e9-9dfe-6071936f1a30.gif" align="center" width="100%"/>

## DoubleLiftLayout Attributes
Attributes | Type | Default | Description
--- | --- | --- | ---
foldedWidth | Dimension | 0 | sets the width size when collapsed.
foldedHeight | Dimension | 0 | ets the height size when collapsed.
horizontalDuration | Long | 500L | sets the duration of horizontal lifting.
verticalDuration | Long | 300L | sets the duration of vertical lifting.
cornerRadius | Dimension | 4dp | sets the corner radius.
autoExpand | Boolean | false | invkoe expand() method initially or not.
startOrientation | LiftStartOrientation | LiftStartOrientation.HORIZONTAL | sets the starting orientation of the lifting.
animation | LiftAnimation | LiftAnimation.NORMAL | sets the lifting animation when expanding and collapsing

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/DoubleLift/stargazers)__ for this repository. :star:<br>
And __[follow](https://github.com/skydoves)__ me for my next creations! 🤩

# License
```xml
Copyright 2019 skydoves (Jaewoong Eum)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

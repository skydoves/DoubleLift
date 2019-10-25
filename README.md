# DoubleLift

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=16"><img alt="API" src="https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">
🦋 Expands and collapses a layout's horizontal and vertical sequentially.
</p>

<p align="center">
<img src="https://user-images.githubusercontent.com/24237865/67579148-dbf95000-f77e-11e9-8e90-20fd64304f66.gif" width="32%"/>
</p>

## Including in your project
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
    implementation "com.github.skydoves:doublelift:1.0.0"
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
  app:doubleLift_startOrientation="horizontal">

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
We can create an instance of `DoubleLiftLayout` using the builder class.
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

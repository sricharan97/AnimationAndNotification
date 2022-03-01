# LoadApp

In this project students will create an app to download a file from Internet by clicking on a custom-built button where:
 - width of the button gets animated from left to right;
 - text gets changed based on different states of the button;
 - circle gets be animated from 0 to 360 degrees

A notification will be sent once the download is complete. When a user clicks on notification, the user lands on detail activity and the notification gets dismissed. In detail activity, the status of the download will be displayed and animated via MotionLayout upon opening the activity.

[The final look of the app](https://gph.is/g/Zywmnre)



### Dependencies

```
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.0.2'
    implementation 'androidx.core:core-ktx:1.0.2'
   
```

## Skills Learned

  1. Utilizing **Notifications** API
  2. Building and using a **Custom view** with custom attributes
  3. Animating view properties using **valueAnimator**
  4. Coordinating animations in the entire layout using **MotionLayout**
  

## License
Please review the following [license agreement](https://bumptech.github.io/glide/dev/open-source-licenses.html)
